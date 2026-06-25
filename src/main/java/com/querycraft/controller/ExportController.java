package com.querycraft.controller;

import com.querycraft.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@Tag(name = "Export", description = "Export APIs")
@CrossOrigin(origins = "*")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/csv/{historyId}")
    @Operation(summary = "Export query result as CSV")
    public ResponseEntity<byte[]> exportCSV(
            @PathVariable Long historyId,
            Authentication authentication) {

        byte[] csvData = exportService.exportAsCSV(historyId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=querycraft_export.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }

    @GetMapping("/pdf/{historyId}")
    @Operation(summary = "Export query result as PDF")
    public ResponseEntity<byte[]> exportPDF(
            @PathVariable Long historyId,
            Authentication authentication) {

        byte[] pdfData = exportService.exportAsPDF(historyId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=querycraft_export.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }
}