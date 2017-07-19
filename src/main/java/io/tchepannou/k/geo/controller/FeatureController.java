package io.tchepannou.k.geo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tchepannou.k.geo.dao.AddressDao;
import io.tchepannou.k.geo.dao.CityDao;
import io.tchepannou.k.geo.dao.CountryDao;
import io.tchepannou.k.geo.dao.FeatureDao;
import io.tchepannou.k.geo.dao.FeatureTypeDao;
import io.tchepannou.k.geo.dao.GeoPointDao;
import io.tchepannou.k.geo.domain.Address;
import io.tchepannou.k.geo.domain.City;
import io.tchepannou.k.geo.domain.Country;
import io.tchepannou.k.geo.domain.Feature;
import io.tchepannou.k.geo.domain.FeatureType;
import io.tchepannou.k.geo.domain.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/geo/v1")
@Api(value = "/geo/v1", description = "City Features: hotels, aiports, station etc.")
public class FeatureController {
    @Autowired
    FeatureDao dao;

    @Autowired
    FeatureTypeDao featureTypeDao;

    @Autowired
    AddressDao addressDao;

    @Autowired
    GeoPointDao geoPointDao;

    @Autowired
    CountryDao countryDao;

    @Autowired
    CityDao cityDao;



    @RequestMapping(path = "/feature/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "findById", notes = "Search feature by ID")
    public ResponseEntity<io.tchepannou.k.geo.Feature> findById(@PathVariable long id){
        final Feature feature = dao.findOne(id);
        if (feature == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        final FeatureType featureType = featureTypeDao.findOne(feature.getFeatureTypeId());
        final Address address = feature.getAddressId() != null ? addressDao.findOne(feature.getAddressId()) : null;
        final GeoPoint geoPoint = feature.getGeoPointId() != null ? geoPointDao.findOne(feature.getGeoPointId()) : null;

        return toResponseEntity(feature, featureType, address, geoPoint);
    }

    @RequestMapping(path = "/features/city/{cityId}/type/{featureTypeName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "findByCityByFeatureType", notes = "Search the features of a city of a given tyoe")
    public ResponseEntity<List<io.tchepannou.k.geo.Feature>> findByCityByFeatureType(
            @PathVariable(name = "cityId") long cityId,
            @PathVariable(name = "featureTypeName") String featureTypeName,
            @RequestParam(defaultValue = "20", required = false) @ApiParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0", required = false) @ApiParam(defaultValue = "0") int page
    ){
        final FeatureType featureType = featureTypeDao.findByNameIgnoreCase(featureTypeName);
        if (featureType == null){
            return new ResponseEntity(Collections.emptyList(), HttpStatus.OK);
        }

        PageRequest pageable = new PageRequest(page, limit, Sort.Direction.ASC, "name");
        final List<Feature> features = dao.findByCityIdAndFeatureTypeId(cityId, featureType.getId(), pageable);
        return toResponseEntity(features, featureType);
    }


    //-- Private
    private ResponseEntity<io.tchepannou.k.geo.Feature> toResponseEntity (
            Feature feature,
            FeatureType featureType,
            Address address,
            GeoPoint geoPoint
    ){
        io.tchepannou.k.geo.Feature featureDto = toDto(feature, featureType, false);
        featureDto.setAddress(toDto(address));
        featureDto.setGeoPoint(toDto(geoPoint));
        return new ResponseEntity(featureDto, HttpStatus.OK);
    }

    private ResponseEntity<List<io.tchepannou.k.geo.Feature>> toResponseEntity (List<Feature> features, FeatureType featureType){
        return new ResponseEntity(
            features.stream()
                    .map(f -> toDto(f, featureType, true))
                    .collect(Collectors.toList()),
            HttpStatus.OK
        );
    }

    private io.tchepannou.k.geo.Feature toDto(Feature feature, FeatureType featureType, boolean summary){
        final io.tchepannou.k.geo.Feature dto = new io.tchepannou.k.geo.Feature();

        dto.setId(feature.getId());
        dto.setCityId(feature.getCityId());
        dto.setName(feature.getName());
        dto.setFeatureType(io.tchepannou.k.geo.Feature.FeatureType.fromValue(featureType.getName()));

        if (!summary) {
            dto.setWebsite(feature.getWebsite());
            dto.setPhone(feature.getPhone());
            dto.setFax(feature.getFax());
            dto.setEmail(feature.getEmail());
            dto.setDescription(feature.getDescription());
        }

        return dto;
    }

    private io.tchepannou.k.geo.Address toDto(Address address){
        final io.tchepannou.k.geo.Address dto = new io.tchepannou.k.geo.Address();
        final Long cityId = address.getCityId();

        dto.setCityId(address.getCityId());
        dto.setPostalCode(address.getPostalCode());
        dto.setStreet(address.getStreet());

        if (cityId != null){
            final City city = cityDao.findOne(cityId);
            final Country country = countryDao.findOne(city.getCountryId());

            dto.setCityId(cityId);
            dto.setCity(city.getAsciiName());
            dto.setCountry(country.getIso());
        }

        return dto;
    }

    private io.tchepannou.k.geo.GeoPoint toDto(GeoPoint geoPoint){
        final io.tchepannou.k.geo.GeoPoint dto = new io.tchepannou.k.geo.GeoPoint();

        dto.setId(geoPoint.getId());
        dto.setLatitude(geoPoint.getLatitude());
        dto.setLongitude(geoPoint.getLongitude());

        return dto;
    }
}
