package com.querycraft.dto.request;

import jakarta.validation.constraints.NotBlank;

public class QueryRequest {

    @NotBlank(message = "Question is required")
    private String question;

    private boolean executeQuery = true;

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public boolean isExecuteQuery() { return executeQuery; }
    public void setExecuteQuery(boolean executeQuery) { this.executeQuery = executeQuery; }
}