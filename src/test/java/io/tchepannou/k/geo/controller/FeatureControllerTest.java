package io.tchepannou.k.geo.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeatureControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        new ResourceDatabasePopulator(
                new ClassPathResource("/sql/clean.sql"),
                new ClassPathResource("/sql/FeatureController.sql")
        ).execute(dataSource);
    }

    @Test
    public void findById() throws Exception {
        mockMvc
                .perform(get("/geo/v1/feature/100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.featureType", is("bus_station")))
                .andExpect(jsonPath("$.name", is("gare1")))
                .andExpect(jsonPath("$.description", is("description1")))
                .andExpect(jsonPath("$.phone", is("1111111")))
                .andExpect(jsonPath("$.fax", is("1111112")))
                .andExpect(jsonPath("$.email", is("gare1@gmail.com")))
                .andExpect(jsonPath("$.website", is("http://www.gare1.com")))
                .andExpect(jsonPath("$.cityId", is(223338700)))
                .andExpect(jsonPath("$.address.street", is("3030 Linton")))
                .andExpect(jsonPath("$.address.postalCode", is("123")))
                .andExpect(jsonPath("$.address.country", is("CM")))
                .andExpect(jsonPath("$.address.cityId", is(223338700)))
                .andExpect(jsonPath("$.address.city", is("Yaounde")))
                .andExpect(jsonPath("$.geoPoint.latitude", is(1d)))
                .andExpect(jsonPath("$.geoPoint.longitude", is(2d)))
        ;
    }

    @Test
    public void findByIdNotFound() throws Exception {
        mockMvc
                .perform(get("/geo/v1/feature/0"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void findByCityByFeatureType() throws Exception {
        mockMvc
                .perform(get("/geo/v1/features/city/223338700/type/bus_station"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))

                .andExpect(jsonPath("$[0].id", is(100)))
                .andExpect(jsonPath("$[0].featureType", is("bus_station")))
                .andExpect(jsonPath("$[0].name", is("gare1")))

                .andExpect(jsonPath("$[1].id", is(101)))
                .andExpect(jsonPath("$[1].featureType", is("bus_station")))
                .andExpect(jsonPath("$[1].name", is("gare2")))
        ;

    }
}
