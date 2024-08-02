package org.sid.kpiservice.service.impl;

import org.sid.kpiservice.dto.KpiDto;
import org.sid.kpiservice.entity.KPI;
import org.sid.kpiservice.mapper.KpiMapper;
import org.sid.kpiservice.repository.KpiRepository;
import org.sid.kpiservice.service.KpiService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KpiServiceImpl implements KpiService{

    private final KpiRepository kpiRepository;


    public KpiServiceImpl(KpiRepository kpiRepository) {

        this.kpiRepository = kpiRepository;
    }
    @Override
    public List<KpiDto> getAllKpis() {
        List<KPI> kpis = kpiRepository.findAll();
        return kpis.stream().map(kpi -> KpiMapper.maptoKpiDto(kpi))
                .collect(Collectors.toList());
    }

    @Override
    public KpiDto getKpiById(Long id) {
        KPI kpi = kpiRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("KPI does not exist : "+ id));
        return KpiMapper.maptoKpiDto(kpi);
    }

    @Override
    public KpiDto createKpi(KpiDto kpiDto) {
        KPI kpi = KpiMapper.maptoKpi(kpiDto);
        KPI createdKpi = kpiRepository.save(kpi);

        return KpiMapper.maptoKpiDto(createdKpi);
    }
    public String getFormulaById(Long id) {
        // Implement logic to fetch formula from repository
        Optional<KPI> formulaEntity = kpiRepository.findById(id);
        return formulaEntity.map(KPI::getFormule).orElse("");
    }

    @Override
    public KpiDto updateKpi(Long id, KpiDto updatedKpi) {
        KPI kpi = kpiRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Id does not exist : " + id)
        );

        kpi.setNom(updatedKpi.getNom());
        kpi.setFormule(updatedKpi.getFormule());

        KPI updatedKpiObj = kpiRepository.save(kpi);

        return KpiMapper.maptoKpiDto(updatedKpiObj);
    }

    @Override
    public void deleteKpi(Long id) {
        kpiRepository.deleteById(id);
    }
}
