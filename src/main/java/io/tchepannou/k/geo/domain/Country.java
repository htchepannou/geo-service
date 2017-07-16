package io.tchepannou.k.geo.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "t_country")
public class Country {
    @Id
    private Long id;
    private String iso;
    private String iso3;
    private String name;
    private Integer area;
    private Integer population;
    private String currencyCode;

    private String languages;

    private String tld;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(final String iso) {
        this.iso = iso;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(final String iso3) {
        this.iso3 = iso3;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(final String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(final String languages) {
        this.languages = languages;
    }

    public String getTld() {
        return tld;
    }

    public void setTld(final String tld) {
        this.tld = tld;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(final Integer area) {
        this.area = area;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(final Integer population) {
        this.population = population;
    }
}
