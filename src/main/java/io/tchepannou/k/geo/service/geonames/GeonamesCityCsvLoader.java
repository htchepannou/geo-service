package io.tchepannou.k.geo.service.geonames;

import com.google.common.base.Strings;
import io.tchepannou.k.geo.dao.CityDao;
import io.tchepannou.k.geo.dao.CountryDao;
import io.tchepannou.k.geo.domain.City;
import io.tchepannou.k.geo.domain.Country;
import io.tchepannou.k.geo.service.CsvLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class GeonamesCityCsvLoader extends CsvLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeonamesCityCsvLoader.class);
    private static final List<String> SUPPORTED_CODES = Arrays.asList("PPL", "PPLC", "PPLS", "PPLA");
    private static final String MODIFICATION_DATE_PATTERN = "yyyy-MM-dd";
    private static final int GEONAMES_ID_COLUMN = 0;
    private static final int FEATURE_CLASS = 6;
    private static final int FEATURE_CODE = 7;
    private static final int COUNTRY_ISO = 8;
    private static final int MODIFICATION_DATE = 18;

    @Autowired
    CityDao dao;

    @Autowired
    CountryDao countryDao;

    @Override
    protected char getSeparator() {
        return '\t';
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected boolean process (int row, String[] cells) {

        if (!"P".equals(cells[FEATURE_CLASS])){
            return false;
        }
        if (!SUPPORTED_CODES.contains(cells[FEATURE_CODE])){
            return false;
        }

        final Long geonamesId = toLong(cells[GEONAMES_ID_COLUMN]);
        if (geonamesId == null){
            warn(row, "geonamesid=null", null);
            return false;
        }

        final String iso = cells[COUNTRY_ISO];
        final Country country = countryDao.findByIsoIgnoreCase(iso);
        if (country == null){
            warn(row, "Country[" + iso + "] not found", null);
            return false;
        }

        City city = dao.findOne(geonamesId);
        if (city == null){
            city = new City();
        } else {
            if (!shouldModifiy(cells[MODIFICATION_DATE], city)){
                return false;
            }
        }

        fromCsv(cells, country, city);
        try {
            dao.save(city);
            return true;

        } catch (DataIntegrityViolationException e){
            // Ignore this exception
        }
        return false;
    }

    protected void fromCsv(final String[] cells, final Country country, final City city){
        city.setId(toLong(cells[GEONAMES_ID_COLUMN]));
        city.setName(cells[1]);
        city.setAsciiName(cells[2]);
        city.setPopulation(toInt(cells[14]));
        city.setTimezone(cells[17]);
        city.setCountryId(country.getId());
        city.setModificationDate(toDate(cells[MODIFICATION_DATE], MODIFICATION_DATE_PATTERN));
    }

    private boolean shouldModifiy(String date, final City city){
        if (Strings.isNullOrEmpty(date) || city.getModificationDate() == null){
            return true;
        }

        final Date modificationDate = toDate(date, MODIFICATION_DATE_PATTERN);
        return modificationDate.getTime() > city.getModificationDate().getTime();
    }
}
