package io.tchepannou.k.geo.dao;

import io.tchepannou.k.geo.domain.City;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CityDao extends CrudRepository<City, Long> {
    City findByAsciiNameIgnoreCase(String asciiName);
    List<City> findByCountryIdAndAsciiNameIgnoreCaseStartsWith(long countryId, String asciiName, Pageable pageable);
}
