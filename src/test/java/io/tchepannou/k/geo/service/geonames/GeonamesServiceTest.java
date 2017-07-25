package io.tchepannou.k.geo.service.geonames;

import com.google.common.collect.Iterables;
import io.tchepannou.k.geo.dao.CityDao;
import io.tchepannou.k.geo.dao.CountryDao;
import io.tchepannou.k.geo.dao.ImportLogDao;
import io.tchepannou.k.geo.dao.LockDao;
import io.tchepannou.k.geo.domain.City;
import io.tchepannou.k.geo.domain.Country;
import io.tchepannou.k.geo.domain.ImportLog;
import io.tchepannou.k.geo.domain.Lock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeonamesServiceTest {

    @Autowired
    GeonamesService geonames;

    @Autowired
    CountryDao countryDao;

    @Autowired
    CityDao cityDao;

    @Autowired
    LockDao lockDao;

    @Autowired
    ImportLogDao importLogDao;

    @Test
    @Sql({"/sql/clean.sql"})
    public void shouldLoadCountries () throws Exception {
        // Given

        // When
        geonames.loadCountries();

        // Then
        final Iterable<Country> countries = countryDao.findAll();
        assertThat(countries).isNotEmpty();


        final Iterable<Lock> locks = lockDao.findAll();
        assertThat(locks).isEmpty();


        final Iterable<ImportLog> logs = importLogDao.findAll();
        assertThat(logs).hasSize(1);

        final ImportLog log = logs.iterator().next();
        assertThat(log.getError()).isNull();
        assertThat(log.getStackTrace()).isNull();
        assertThat(log.getRows()).isEqualTo(Iterables.size(countries));
        assertThat(log.getUrl()).isEqualTo(geonames.getBaseUrl() + "/countryInfo.txt");
    }

    @Test
    @Sql({"/sql/clean.sql"})
    public void shouldNotLoadCountriesIfLock () throws Exception {
        // Given
        lockDao.save(new Lock(GeonamesService.LOCK_COUNTRY, "test"));

        // When
        geonames.loadCountries();

        // Then
        final Iterable<Country> countries = countryDao.findAll();
        assertThat(countries).isEmpty();


        final Iterable<Lock> locks = lockDao.findAll();
        assertThat(locks).hasSize(1);


        final Iterable<ImportLog> logs = importLogDao.findAll();
        assertThat(logs).hasSize(0);
    }

    @Test
    @Sql({"/sql/clean.sql"})
    public void shouldLoadCities () throws Exception {
        // Given
        final Country country = new Country();
        country.setId(1L);
        country.setIso("CM");
        country.setIso3("CMR");
        country.setName("Cameroon");
        countryDao.save(country);

        // When
        geonames.loadCities();

        // Then
        final Iterable<City> cities = cityDao.findAll();
        assertThat(cities).isNotEmpty();


        final Iterable<Lock> locks = lockDao.findAll();
        assertThat(locks).isEmpty();


        final Iterable<ImportLog> logs = importLogDao.findAll();
        assertThat(logs).hasSize(1);

        final ImportLog log = logs.iterator().next();
        assertThat(log.getError()).isNull();
        assertThat(log.getStackTrace()).isNull();
        assertThat(log.getRows()).isEqualTo(Iterables.size(cities));
        assertThat(log.getUrl()).isEqualTo(geonames.getBaseUrl() + "/CM.zip");
    }

    @Test
    @Sql({"/sql/clean.sql"})
    public void shouldLoadCitiesTwice () throws Exception {
        // Given
        final Country country = new Country();
        country.setId(1L);
        country.setIso("CM");
        country.setIso3("CMR");
        country.setName("Cameroon");
        countryDao.save(country);

        // When
        geonames.loadCities();
        geonames.loadCities();

        // Then
    }

}
