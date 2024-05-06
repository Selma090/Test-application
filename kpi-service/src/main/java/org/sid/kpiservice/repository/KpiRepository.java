package org.sid.kpiservice.repository;

import org.sid.kpiservice.entity.KPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KpiRepository extends JpaRepository<KPI, Long> {

}
