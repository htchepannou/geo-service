package io.tchepannou.k.geo.dao;

import io.tchepannou.k.geo.domain.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CountryDao extends CrudRepository<Country, Long> {
    Country findByIsoIgnoreCase(String iso);
    Country findByIso3IgnoreCase(String iso3);
}
