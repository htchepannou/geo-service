package io.tchepannou.k.geo.service.geonames;

import io.tchepannou.k.geo.dao.CityDao;
import io.tchepannou.k.geo.domain.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/sql/clean.sql", "/sql/GeonamesCityCsvLoader.sql"})
public class GeonamesCityCsvLoaderTest {

    @Autowired
    GeonamesCityCsvLoader loader;

    @Autowired
    CityDao dao;

    @Test
    public void shouldLoadAllCMCities() throws Exception {
        InputStream in = getClass().getResourceAsStream("/CM-features.tsv");

        loader.load(in);

        City city = dao.findByAsciiNameIgnoreCase("Douala");
        assertThat(city).isNotNull();
        assertThat(city.getAsciiName()).isEqualTo("Douala");
        assertThat(city.getName()).isEqualTo("Douala");
        assertThat(city.getTimezone()).isEqualTo("Africa/Douala");
        assertThat(city.getPopulation()).isGreaterThan(0);

        city = dao.findByAsciiNameIgnoreCase("yaounde");
        assertThat(city).isNotNull();
        assertThat(city.getAsciiName()).isEqualTo("Yaounde");
        assertThat(city.getName()).isEqualTo("Yaoundé");
        assertThat(city.getTimezone()).isEqualTo("Africa/Douala");
        assertThat(city.getPopulation()).isGreaterThan(0);
    }
}
