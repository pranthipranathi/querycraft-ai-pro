package com.querycraft.controller;

import com.querycraft.dto.response.SchemaResponse;
import com.querycraft.service.SchemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schema")
@Tag(name = "Schema", description = "Database Schema APIs")
@CrossOrigin(origins = "*")
public class SchemaController {

    @Autowired
    private SchemaService schemaService;

    @GetMapping("/tables")
    @Operation(summary = "Get all table names")
    public ResponseEntity<List<String>> getAllTables() {
        return ResponseEntity.ok(schemaService.getAllTableNames());
    }

    @GetMapping("/tables/{tableName}")
    @Operation(summary = "Get schema of a specific table")
    public ResponseEntity<SchemaResponse> getTableSchema(
            @PathVariable String tableName) {
        return ResponseEntity.ok(
                schemaService.getTableSchema(tableName));
    }
}