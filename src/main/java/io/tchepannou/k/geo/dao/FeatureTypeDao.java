package io.tchepannou.k.geo.dao;

import io.tchepannou.k.geo.domain.FeatureType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FeatureTypeDao extends CrudRepository<FeatureType, Long> {
    FeatureType findByNameIgnoreCase (String name);
}
