package io.tchepannou.k.geo.dao;

import io.tchepannou.k.geo.domain.ImportLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ImportLogDao extends CrudRepository<ImportLog, Long> {
}
