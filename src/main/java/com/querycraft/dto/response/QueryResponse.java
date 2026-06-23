package com.querycraft.dto.response;

import java.util.List;
import java.util.Map;

public class QueryResponse {

    private String naturalLanguageQuery;
    private String generatedSql;
    private String explanation;
    private boolean isSafe;
    private String safetyMessage;
    private List<Map<String, Object>> results;
    private Integer rowCount;
    private Long executionTimeMs;
    private String status;
    private String errorMessage;

    public QueryResponse() {}

    public String getNaturalLanguageQuery() { return naturalLanguageQuery; }
    public void setNaturalLanguageQuery(String naturalLanguageQuery) {
        this.naturalLanguageQuery = naturalLanguageQuery;
    }

    public String getGeneratedSql() { return generatedSql; }
    public void setGeneratedSql(String generatedSql) { this.generatedSql = generatedSql; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public boolean isSafe() { return isSafe; }
    public void setSafe(boolean safe) { isSafe = safe; }

    public String getSafetyMessage() { return safetyMessage; }
    public void setSafetyMessage(String safetyMessage) { this.safetyMessage = safetyMessage; }

    public List<Map<String, Object>> getResults() { return results; }
    public void setResults(List<Map<String, Object>> results) { this.results = results; }

    public Integer getRowCount() { return rowCount; }
    public void setRowCount(Integer rowCount) { this.rowCount = rowCount; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}