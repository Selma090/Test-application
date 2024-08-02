package org.sid.kpiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiDto {

    private Long id;
    private String nom;
    private String formule;
}
