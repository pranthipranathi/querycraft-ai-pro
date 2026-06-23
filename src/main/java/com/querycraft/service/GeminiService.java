package com.querycraft.service;

import com.querycraft.exception.GeminiAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    public GeminiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String generateSQL(String userQuestion, String schema) {
        String prompt = """
                You are an expert SQL developer.
                Given the following database schema:
                
                %s
                
                Convert this natural language question to a SQL query:
                "%s"
                
                Rules:
                1. Generate ONLY a SELECT query
                2. Return ONLY the SQL query, nothing else
                3. No explanations, no markdown, no backticks
                4. End the query with semicolon
                """.formatted(schema, userQuestion);

        return callGeminiAPI(prompt);
    }

    public String explainSQL(String sql) {
        try {
            String prompt = """
                    Explain this SQL query in simple English 
                    that a non-technical person can understand.
                    Keep it to 2-3 sentences maximum.
                    
                    SQL: %s
                    
                    Explanation:
                    """.formatted(sql);

            return callGeminiAPI(prompt);
        } catch (Exception e) {
            return "This query retrieves data from the database based on your question.";
        }
    }

    private String callGeminiAPI(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    )
            );

            Map response = webClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("candidates")) {
                List candidates = (List) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map content = (Map) candidate.get("content");
                    List parts = (List) content.get("parts");
                    Map part = (Map) parts.get(0);
                    return part.get("text").toString().trim();
                }
            }
            throw new GeminiAPIException("Empty response from Gemini API");

        } catch (GeminiAPIException e) {
            throw e;
        } catch (Exception e) {
            throw new GeminiAPIException(
                    "Gemini API error: " + e.getMessage());
        }
    }
}