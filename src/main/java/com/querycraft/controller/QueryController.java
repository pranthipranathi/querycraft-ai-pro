package com.querycraft.controller;

import com.querycraft.dto.request.QueryRequest;
import com.querycraft.dto.response.QueryResponse;
import com.querycraft.service.QueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/query")
@Tag(name = "Query", description = "Natural Language to SQL APIs")
@CrossOrigin(origins = "*")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @PostMapping("/generate")
    @Operation(summary = "Convert natural language to SQL and execute")
    public ResponseEntity<QueryResponse> processQuery(
            @Valid @RequestBody QueryRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                queryService.processQuery(request, username));
    }
}