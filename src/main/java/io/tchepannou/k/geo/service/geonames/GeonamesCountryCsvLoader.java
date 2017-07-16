package io.tchepannou.k.geo.service.geonames;

import io.tchepannou.k.geo.dao.CountryDao;
import io.tchepannou.k.geo.domain.Country;
import io.tchepannou.k.geo.service.CsvLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeonamesCountryCsvLoader extends CsvLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeonamesCountryCsvLoader.class);
    private static final int GEONAMES_ID_COLUMN = 16;

    @Autowired
    CountryDao dao;

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

        final Long geonamesId = toLong(cells[GEONAMES_ID_COLUMN]);
        if (geonamesId == null){
            warn(row, "geonamesid=null", null);
            return false;
        }

        Country country = dao.findOne(geonamesId);
        if (country == null){
            country = new Country();
        }

        fromCsv(cells, country);
        dao.save(country);
        return true;
    }

    protected void fromCsv(String[] cells, final Country country){
        country.setIso(cells[0]);
        country.setIso3(cells[1]);
        country.setName(cells[4]);
        country.setArea(toInt(cells[6]));
        country.setPopulation(toInt(cells[7]));
        country.setTld(cells[9]);
        country.setCurrencyCode(cells[10]);
        country.setLanguages(cells[15]);
        country.setId(toLong(cells[GEONAMES_ID_COLUMN]));
    }
}
