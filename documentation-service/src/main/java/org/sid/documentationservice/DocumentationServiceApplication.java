package org.sid.documentationservice;

import org.sid.documentationservice.service.DocumentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api/documentation")
public class DocumentationServiceApplication {

	private final DocumentationService documentationService;

	@Autowired
	public DocumentationServiceApplication(DocumentationService documentationService) {
		this.documentationService = documentationService;
	}

	@PostMapping
	public ResponseEntity<Map<String, String>> uploadPdf(@RequestParam("pdf") MultipartFile file) {
		try {
			String uploadMessage = documentationService.uploadPdf(file);
			Map<String, String> response = new HashMap<>();
			response.put("message", uploadMessage);
			return ResponseEntity.ok(response);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload PDF", e);
		}
	}

	@GetMapping("/{fileName}")
	public ResponseEntity<byte[]> downloadPdf(@PathVariable String fileName) {
		try {
			byte[] pdfData = documentationService.downloadPdf(fileName);
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_PDF)
					.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
					.body(pdfData);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PDF not found", e);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePdf(@PathVariable Long id) {
		try {
			documentationService.deletePdf(id);
			return ResponseEntity.ok("PDF deleted successfully");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete PDF", e);
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<String>> listFiles() {
		try {
			List<String> fileNames = documentationService.listFiles();
			return ResponseEntity.ok(fileNames);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to list files", e);
		}
	}

	@GetMapping("/test")
	public ResponseEntity<String> testEndpoint() {
		return ResponseEntity.ok("Test endpoint is working!");
	}

	public static void main(String[] args) {
		SpringApplication.run(DocumentationServiceApplication.class, args);
	}
}