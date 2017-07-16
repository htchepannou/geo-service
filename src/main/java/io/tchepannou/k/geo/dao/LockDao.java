package io.tchepannou.k.geo.dao;

import io.tchepannou.k.geo.domain.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LockDao extends CrudRepository<Lock, Long> {
}
