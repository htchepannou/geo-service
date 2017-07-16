package io.tchepannou.k.geo.service.geonames;

import io.tchepannou.k.geo.dao.CountryDao;
import io.tchepannou.k.geo.domain.Country;
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
public class GeonamesCountryCsvLoaderTest {

    @Autowired
    GeonamesCountryCsvLoader loader;

    @Autowired
    CountryDao dao;

    @Test
    public void shouldLoadAllCountries() throws Exception {
        InputStream in = getClass().getResourceAsStream("/country-all.tsv");

        loader.load(in);

        final Country cm = dao.findByIsoIgnoreCase("CM");

        assertThat(cm.getId()).isEqualTo(2233387);
        assertThat(cm.getArea()).isEqualTo(475440);
        assertThat(cm.getCurrencyCode()).isEqualTo("XAF");
        assertThat(cm.getIso()).isEqualTo("CM");
        assertThat(cm.getIso3()).isEqualTo("CMR");
        assertThat(cm.getLanguages()).contains("fr-CM", "en-CM");
        assertThat(cm.getName()).contains("Cameroon");
        assertThat(cm.getTld()).contains(".cm");
        assertThat(cm.getPopulation()).isGreaterThanOrEqualTo(19294149);
    }
}
