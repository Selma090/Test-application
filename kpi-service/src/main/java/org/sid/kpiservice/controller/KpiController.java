package org.sid.kpiservice.controller;

import org.sid.kpiservice.dto.KpiDto;
import org.sid.kpiservice.service.KpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/kpis")
public class KpiController {
    private final KpiService kpiService;

    @Autowired
    public KpiController(KpiService kpiService) {

        this.kpiService = kpiService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<KpiDto>> getAllKpis() {
        List<KpiDto> kpis = kpiService.getAllKpis();
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KpiDto> getKpiById(@PathVariable("id") Long id) {

        KpiDto kpiDto = kpiService.getKpiById(id);
        return ResponseEntity.ok(kpiDto);
    }

    @PostMapping("/create")
    public ResponseEntity<KpiDto> createKpi(@RequestBody KpiDto kpiDto) {
        KpiDto createdKpi = kpiService.createKpi(kpiDto);
        return new ResponseEntity<>(createdKpi, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<KpiDto> updateKpi(@PathVariable("id") Long id, @RequestBody @Valid KpiDto updatedkpi) {
        KpiDto kpiDto = kpiService.updateKpi(id, updatedkpi);
        return ResponseEntity.ok(kpiDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteKpi(@PathVariable Long id) {
        kpiService.deleteKpi(id);
        return ResponseEntity.noContent().build();
    }
}
