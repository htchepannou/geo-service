package io.tchepannou.k.geo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tchepannou.k.geo.dao.CountryDao;
import io.tchepannou.k.geo.domain.Country;
import io.tchepannou.k.geo.service.geonames.GeonamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/geo/v1")
@Api(value = "/geo/v1", description = "Countries")
public class CountryController {
    @Autowired
    CountryDao dao;

    @Autowired
    GeonamesService service;

    @RequestMapping(path = "/country/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "findById", notes = "Search a country by ID")
    public ResponseEntity<io.tchepannou.k.geo.Country> findById(@PathVariable String id){
        try {
            final Long pk = Long.parseLong(id);
            final Country country = dao.findOne(pk);

            return toResponseEntity(country);
        } catch (Exception e){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/country/iso/{iso}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "findByIso", notes = "Search a country by ISO code (ISO2 or ISO3)")
    public ResponseEntity<io.tchepannou.k.geo.Country> findByIso(@PathVariable String iso){
        final Country country = iso.length() == 2
                ? dao.findByIsoIgnoreCase(iso)
                : dao.findByIso3IgnoreCase(iso);

        return toResponseEntity(country);
    }

    @RequestMapping(path = "/countries/load", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "load", notes = "Load all the countries")
    public void load() throws IOException{
        service.loadCountries();
    }

    private ResponseEntity<io.tchepannou.k.geo.Country> toResponseEntity (Country country){
        if (country == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(toDto(country), HttpStatus.OK);
        }
    }

    private io.tchepannou.k.geo.Country toDto (Country country){
        final io.tchepannou.k.geo.Country dto = new io.tchepannou.k.geo.Country();

        dto.setArea(country.getArea());
        dto.setCurrencyCode(country.getCurrencyCode());
        dto.setId(country.getId());
        dto.setIso(country.getIso());
        dto.setIso3(country.getIso3());
        dto.setLanguages(csv2List(country.getLanguages()));
        dto.setName(country.getName());
        dto.setPopulation(country.getPopulation());
        dto.setTopLevelDomain(country.getTld());

        return dto;
    }

    private List<String> csv2List(final String value){
        if (value == null){
            return null;
        }
        return Arrays.asList(value.split(","));
    }
}
