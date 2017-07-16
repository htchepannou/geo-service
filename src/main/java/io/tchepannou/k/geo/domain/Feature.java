package io.tchepannou.k.geo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "T_FEATURE")
public class Feature {
    @Id
    private Long id;

    @Column(name="feature_type_fk")
    private Long featureTypeId;

    @Column(name="address_fk")
    private Long addressId;

    @Column(name="geo_point_fk")
    private Long geoPointId;

    @Column(name="city_fk")
    private Long cityId;


    private String name;
    private String description;
    private String fax;
    private String phone;
    private String email;
    private String website;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(final String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }


    public Long getFeatureTypeId() {
        return featureTypeId;
    }

    public void setFeatureTypeId(final Long featureTypeId) {
        this.featureTypeId = featureTypeId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(final Long addressId) {
        this.addressId = addressId;
    }

    public Long getGeoPointId() {
        return geoPointId;
    }

    public void setGeoPointId(final Long geoPointId) {
        this.geoPointId = geoPointId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(final Long cityId) {
        this.cityId = cityId;
    }
}
