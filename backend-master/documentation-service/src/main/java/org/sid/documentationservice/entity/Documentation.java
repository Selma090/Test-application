package org.sid.documentationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.File;

@Entity
@Table(name = "PdfData")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "pdfdata", nullable = false)
    private byte[] pdfData;
}
