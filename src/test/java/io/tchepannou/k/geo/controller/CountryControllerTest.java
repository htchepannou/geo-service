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
public class CountryControllerTest {
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
                new ClassPathResource("/sql/CountryController.sql")
        ).execute(dataSource);
    }

    @Test
    public void findById() throws Exception {
        mockMvc
                .perform(get("/geo/v1/country/" + 2233387))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cameroon")))
                .andExpect(jsonPath("$.iso", is("CM")))
                .andExpect(jsonPath("$.iso3", is("CMR")))
                .andExpect(jsonPath("$.currencyCode", is("XAF")))
                .andExpect(jsonPath("$.population", is(19294149)))
                .andExpect(jsonPath("$.area", is(475440)))
        ;
    }

    @Test
    public void findByIdNotFound() throws Exception {
        mockMvc
                .perform(get("/geo/v1/country/0"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void findByIso() throws Exception {
        mockMvc
                .perform(get("/geo/v1/country/iso/CM"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cameroon")))
                .andExpect(jsonPath("$.iso", is("CM")))
                .andExpect(jsonPath("$.iso3", is("CMR")))
                .andExpect(jsonPath("$.currencyCode", is("XAF")))
                .andExpect(jsonPath("$.population", is(19294149)))
                .andExpect(jsonPath("$.area", is(475440)))
        ;
    }

    @Test
    public void findByIsoNotFound() throws Exception {
        mockMvc
                .perform(get("/geo/v1/country/iso/--"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

}
