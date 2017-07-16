package io.tchepannou.k.geo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table( name = "t_city")
public class City {
    @Id
    private Long id;

    private String name;

    @Column(name="country_fk")
    private Long countryId;

    @Column(name="ascii_name")
    private String asciiName;

    private String timezone;
    private Integer population;

    @Column(name="modification_date")
    private Date modificationDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAsciiName() {
        return asciiName;
    }

    public void setAsciiName(final String asciiName) {
        this.asciiName = asciiName;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(final Integer population) {
        this.population = population;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(final Long countryId) {
        this.countryId = countryId;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(final Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}
