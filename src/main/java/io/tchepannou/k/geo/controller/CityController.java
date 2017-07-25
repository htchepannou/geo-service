package io.tchepannou.k.geo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tchepannou.k.geo.dao.CityDao;
import io.tchepannou.k.geo.dao.CountryDao;
import io.tchepannou.k.geo.domain.City;
import io.tchepannou.k.geo.domain.Country;
import io.tchepannou.k.geo.service.geonames.GeonamesService;
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

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/geo/v1")
@Api(value = "/geo/v1", description = "Cities")
public class CityController {
    @Autowired
    CountryDao countryDao;

    @Autowired
    CityDao dao;

    @Autowired
    GeonamesService service;

    @RequestMapping(path = "/city/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "findById", notes = "Search city by ID")
    public ResponseEntity<io.tchepannou.k.geo.City> findById(@PathVariable String id){
        try {
            final Long pk = Long.parseLong(id);
            final City city = dao.findOne(pk);
            final Country country = countryDao.findOne(city.getCountryId());

            return toResponseEntity(city, country);
        } catch (Exception e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/cities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search", notes = "Search city by keyword")
    public ResponseEntity<List<io.tchepannou.k.geo.City>> search(
            @RequestParam(name = "q", required = true) String keyword,
            @RequestParam(name = "country", required = true) String countryCode,
            @RequestParam(defaultValue = "20", required = false) @ApiParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0", required = false) @ApiParam(defaultValue = "0") int page
    ){
        final Country country = countryCode.length() == 2
                ? countryDao.findByIsoIgnoreCase(countryCode)
                : countryDao.findByIso3IgnoreCase(countryCode);
        if (country == null){
            return new ResponseEntity(Collections.emptyList(), HttpStatus.OK);
        }

        PageRequest pageable = new PageRequest(page, limit, Sort.Direction.ASC, "asciiName");
        List<City> cities = dao.findByCountryIdAndAsciiNameIgnoreCaseStartsWith(country.getId(), keyword, pageable);

        return new ResponseEntity(cities.stream()
                .map(c -> toDto(c, country))
                .collect(Collectors.toList()),
            HttpStatus.OK
        );
    }


    @RequestMapping(path = "/cities/load/{countryISO}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "load", notes = "Load all the cities of a country")
    public void load(@PathVariable String countryISO) throws IOException {
        service.loadCities(countryISO);
    }

    private ResponseEntity<io.tchepannou.k.geo.City> toResponseEntity (City city, Country country){
        if (city == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(toDto(city, country), HttpStatus.OK);
        }
    }

    private io.tchepannou.k.geo.City toDto (City city, Country country){
        final io.tchepannou.k.geo.City dto = new io.tchepannou.k.geo.City();

        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setAsciiName(city.getAsciiName());
        dto.setPopulation(city.getPopulation());
        dto.setCountry(country.getIso());
        dto.setTimeZone(city.getTimezone());
        return dto;
    }

}
