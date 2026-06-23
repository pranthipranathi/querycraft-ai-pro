package com.querycraft.service;

import com.querycraft.dto.request.QueryRequest;
import com.querycraft.dto.response.QueryResponse;
import com.querycraft.entity.QueryHistory;
import com.querycraft.exception.SQLBlockedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryService {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private SQLValidationService sqlValidationService;

    @Autowired
    private SQLExecutionService sqlExecutionService;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private HistoryService historyService;

    public QueryResponse processQuery(QueryRequest request, String username) {
        QueryResponse response = new QueryResponse();
        response.setNaturalLanguageQuery(request.getQuestion());

        long startTime = System.currentTimeMillis();

        try {
            // Step 1: Get database schema
            String schema = schemaService.getSchemaAsString();

            // Step 2: Generate SQL using Gemini
            String generatedSql = geminiService.generateSQL(
                    request.getQuestion(), schema);
            response.setGeneratedSql(generatedSql);

            // Step 3: Validate SQL safety
            boolean isSafe = sqlValidationService.isSafe(generatedSql);
            response.setSafe(isSafe);

            if (!isSafe) {
                response.setSafetyMessage(
                        "Query blocked for safety reasons!");
                response.setStatus("BLOCKED");

                historyService.saveHistory(username,
                        request.getQuestion(), generatedSql,
                        null, QueryHistory.ExecutionStatus.BLOCKED,
                        0, null, "SQL blocked for safety");
                return response;
            }

            response.setSafetyMessage("Query is safe to execute!");

            // Step 4: Explain SQL
            String explanation = geminiService.explainSQL(generatedSql);
            response.setExplanation(explanation);

            // Step 5: Execute SQL
            if (request.isExecuteQuery()) {
                List<Map<String, Object>> results =
                        sqlExecutionService.executeQuery(generatedSql);
                response.setResults(results);
                response.setRowCount(results.size());
            }

            long executionTime = System.currentTimeMillis() - startTime;
            response.setExecutionTimeMs(executionTime);
            response.setStatus("SUCCESS");

            // Step 6: Save to history
            historyService.saveHistory(username,
                    request.getQuestion(), generatedSql,
                    explanation, QueryHistory.ExecutionStatus.SUCCESS,
                    response.getRowCount(), executionTime, null);

        } catch (SQLBlockedException e) {
            response.setStatus("BLOCKED");
            response.setSafe(false);
            response.setSafetyMessage(e.getMessage());
            historyService.saveHistory(username,
                    request.getQuestion(),
                    response.getGeneratedSql() != null ?
                            response.getGeneratedSql() : "N/A",
                    null, QueryHistory.ExecutionStatus.BLOCKED,
                    0, null, e.getMessage());

        } catch (Exception e) {
            response.setStatus("FAILED");
            response.setErrorMessage(e.getMessage());
            historyService.saveHistory(username,
                    request.getQuestion(),
                    response.getGeneratedSql() != null ?
                            response.getGeneratedSql() : "N/A",
                    null, QueryHistory.ExecutionStatus.FAILED,
                    0, null, e.getMessage());
        }

        return response;
    }
}