package io.tchepannou.k.geo.dao;

import io.tchepannou.k.geo.domain.Feature;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface FeatureDao extends CrudRepository<Feature, Long> {
    List<Feature> findByCityIdAndFeatureTypeId (Long cityId, Long featureTypeId, Pageable pageable);
}
