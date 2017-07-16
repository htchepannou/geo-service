package io.tchepannou.k.geo.service.geonames;

import io.tchepannou.k.geo.dao.ImportLogDao;
import io.tchepannou.k.geo.dao.LockDao;
import io.tchepannou.k.geo.domain.ImportLog;
import io.tchepannou.k.geo.domain.Lock;
import io.tchepannou.k.geo.service.DownloadService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@ConfigurationProperties("k.geonames")
public class Geonames {
    //-- Attributes
    private static final Logger LOGGER = LoggerFactory.getLogger(Geonames.class);

    public static final String LOCK_COUNTRY = "country";
    public static final String LOCK_CITY = "city";

    @Autowired
    LockDao lockDao;

    @Autowired
    ImportLogDao importLogDao;

    @Autowired
    DownloadService downloadService;

    @Autowired
    GeonamesCountryCsvLoader countryLoader;

    @Autowired
    GeonamesCityCsvLoader cityLoader;

    private String baseUrl;
    private List<String> countries;


    //-- Public
    @Scheduled(cron = "0 0 1 ? * *")    // Everyday at 1:00AM
    public void loadCountries() throws IOException{
        final String job = LOCK_COUNTRY;
        Lock lock = lock(job);
        if (lock == null){
            return;
        }

        int rows = 0;
        final URL url = new URL(baseUrl + "/countryInfo.txt");
        LOGGER.info("Loading countries from {}", url);
        try {

            final File file = download(job, url, ".tsv");

            try(FileInputStream in = new FileInputStream(file)){
                rows = countryLoader.load(in);
                log(url, rows, null);
                LOGGER.info("{} countries countries loaded", rows);
            }

        } catch (Exception ex){
            log(url, rows, ex);
        } finally{
            unlock(lock);
        }

    }

    @Scheduled(cron = "0 15 1 ? * *")    // Everyday at 1:15AM
    public void loadCities() throws IOException{
        for (String country : countries) {
            loadCities(country);
        }
    }


    //-- Private
    private int loadCities(final String country) throws IOException{
        final String job = LOCK_CITY + "-" + country;
        Lock lock = lock(job);
        if (lock == null){
            return 0;
        }

        int rows = 0;
        final URL url = new URL(baseUrl + "/" + country + ".zip");
        LOGGER.info("Loading cities of {} from {}", country, url);
        try {

            final File zfile = download(job, url, ".zip");
            final File file = new File(zfile.getParent(), country + ".txt");
            final ZipInputStream zis = new ZipInputStream(new FileInputStream(zfile));
            ZipEntry zipEntry = zis.getNextEntry();
            while(zipEntry != null){

                if (zipEntry.getName().equals(country + ".txt")){
                    /* unzip */
                    try(FileOutputStream out = new FileOutputStream(file)) {
                        IOUtils.copy(zis, out);
                    }

                    /* load */
                    try (FileInputStream in = new FileInputStream(file)){
                        rows = cityLoader.load(in);
                        log(url, rows, null);
                        LOGGER.info("{} cities of {} loaded", rows, country);
                    }
                    break;
                }

                zipEntry = zis.getNextEntry();
            }

        } catch (Exception ex){
            log(url, rows, ex);
        } finally{
            unlock(lock);
        }

        return rows;
    }

    private Lock lock(String name){
        String owner;
        try {
            owner = Inet4Address.getLocalHost().getHostName();
        } catch (Exception e){
            owner = "127.0.0.1";
        }

        try {
            Lock lock = new Lock(name, owner);
            lockDao.save(lock);
            return lock;
        } catch (DataIntegrityViolationException e){
            return null;
        }
    }

    private void unlock(Lock lock){
        lockDao.delete(lock);
    }

    private File download(String job, URL url, String extension) throws IOException {
        File file = File.createTempFile(job, extension);
        try (FileOutputStream out = new FileOutputStream(file)){
            downloadService.download(url, out);
        }
        return file;
    }

    private void log(URL url, int rows, Throwable ex){
        ImportLog importLog = new ImportLog();
        importLog.setRows(rows);
        importLog.setUrl(url.toString());
        if (ex == null) {
            importLog.setSuccess(true);
        } else {
            importLog.setSuccess(false);
            importLog.setError(ex.getMessage());
            importLog.setStackTrace(ExceptionUtils.getStackTrace(ex));
        }
        importLogDao.save(importLog);
    }

    //-- Getter/Setter
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(final List<String> countries) {
        this.countries = countries;
    }
}
