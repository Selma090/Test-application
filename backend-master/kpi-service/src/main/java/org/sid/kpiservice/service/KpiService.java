package org.sid.kpiservice.service;

import org.sid.kpiservice.dto.KpiDto;

import java.util.List;

public interface KpiService {

    List<KpiDto> getAllKpis();

    KpiDto getKpiById(Long id);

    KpiDto createKpi(KpiDto kpiDto);

    KpiDto updateKpi(Long id, KpiDto updatedKpi);

    void deleteKpi(Long id);
}
