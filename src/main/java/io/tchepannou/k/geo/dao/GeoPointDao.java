package io.tchepannou.k.geo.dao;

import io.tchepannou.k.geo.domain.GeoPoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface GeoPointDao extends CrudRepository<GeoPoint, Long> {
}
