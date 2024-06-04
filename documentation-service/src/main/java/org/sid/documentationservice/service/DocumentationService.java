package org.sid.documentationservice.service;

import org.sid.documentationservice.entity.Documentation;
import org.sid.documentationservice.repository.DocumentationRepository;
import org.sid.documentationservice.util.PdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentationService {

    @Autowired
    private DocumentationRepository documentationRepository;

    public String uploadPdf(MultipartFile file) throws IOException {

        Documentation pdfData = documentationRepository.save(Documentation.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .pdfData(PdfUtils.compressPdf(file.getBytes())).build());
        if (pdfData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    public byte[] downloadPdf(String fileName){
        Optional<Documentation> dbPdfData = documentationRepository.findByName(fileName);
        byte[] pdfs=PdfUtils.decompressPdf(dbPdfData.get().getPdfData());
        return pdfs;
    }

    public void deletePdf(Long id) {
        try {
            documentationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            // Handle case where the entity with the given ID doesn't exist
            throw new EntityNotFoundException("Documentation with ID " + id + " not found");
        } catch (Exception e) {
            // Handle other unexpected exceptions
            throw new RuntimeException("Failed to delete PDF with ID " + id, e);
        }
    }

    public List<String> listFiles() {
        return documentationRepository.findAll().stream()
                .map(Documentation::getName)
                .collect(Collectors.toList());
    }


}