package org.sid.kpiservice.mapper;

import org.sid.kpiservice.dto.KpiDto;
import org.sid.kpiservice.entity.KPI;

public class KpiMapper {

    public static KpiDto maptoKpiDto(KPI kpi){
        return new KpiDto(
                kpi.getId(),
                kpi.getNom(),
                kpi.getFormule()
        );
    }

    public static KPI maptoKpi(KpiDto kpiDto){
        return new KPI(
                kpiDto.getId(),
                kpiDto.getNom(),
                kpiDto.getFormule()
        );
    }
}
