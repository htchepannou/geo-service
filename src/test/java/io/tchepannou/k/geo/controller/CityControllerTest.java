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
public class CityControllerTest {
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
                new ClassPathResource("/sql/CityController.sql")
        ).execute(dataSource);
    }

    @Test
    public void findById() throws Exception {
        mockMvc
                .perform(get("/geo/v1/city/" + 223338700))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Yaounde")))
                .andExpect(jsonPath("$.asciiName", is("Yaounde")))
                .andExpect(jsonPath("$.country", is("CM")))
                .andExpect(jsonPath("$.population", is(1239475)))
                .andExpect(jsonPath("$.timeZone", is("Africa/Douala")))
        ;
    }

    @Test
    public void findByIdNotFound() throws Exception {
        mockMvc
                .perform(get("/geo/v1/city/0"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void search() throws Exception {
        mockMvc
                .perform(get("/geo/v1/cities?q=ba&country=CM"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].name", is("Bafia")))
                .andExpect(jsonPath("$[1].name", is("Bafoussam")))
                .andExpect(jsonPath("$[2].name", is("Bangoua")))
        ;
    }

    @Test
    public void searchInvalidCountry() throws Exception {
        mockMvc
                .perform(get("/geo/v1/cities?q=ba&country=--"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)))
        ;
    }

    @Test
    public void searchNotFound() throws Exception {
        mockMvc
                .perform(get("/geo/v1/cities?q=--&country=CM"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)))
        ;
    }
}
