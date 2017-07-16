package io.tchepannou.k.geo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HealthTest {
    @Autowired
    DataSource  dataSource;

    @Test
    public void listTables(){
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        System.out.println("DATABASE");
        jdbc.query("SELECT DATABASE();",
                (rs) -> {
                    System.out.println(rs.getString(1));
                });

        System.out.println("\n\nTABLES");
        jdbc.query("SHOW TABLES;",
                (rs) -> {
                    System.out.println(rs.getString(1));
                });
    }
}
