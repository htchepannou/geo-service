package io.tchepannou.k.geo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "t_address")
public class Address {
    @Id
    private Long id;

    @Column(name="city_fk")
    private Long cityId;

    private String street;

    @Column(name="postal_code")
    private String postalCode;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(final Long cityId) {
        this.cityId = cityId;
    }


}
