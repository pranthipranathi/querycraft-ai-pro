package com.querycraft.controller;

import com.querycraft.dto.response.ApiResponse;
import com.querycraft.entity.QueryHistory;
import com.querycraft.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@Tag(name = "History", description = "Query History APIs")
@CrossOrigin(origins = "*")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping
    @Operation(summary = "Get current user query history")
    public ResponseEntity<List<QueryHistory>> getHistory(
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(
                historyService.getUserHistory(username));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a history entry")
    public ResponseEntity<ApiResponse> deleteHistory(
            @PathVariable Long id) {
        historyService.deleteHistory(id);
        return ResponseEntity.ok(
                ApiResponse.success("History deleted!", null));
    }
}