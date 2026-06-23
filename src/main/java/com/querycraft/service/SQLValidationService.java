package com.querycraft.service;

import com.querycraft.exception.SQLBlockedException;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SQLValidationService {

    private static final List<String> BLOCKED_KEYWORDS = Arrays.asList(
            "INSERT", "UPDATE", "DELETE", "DROP", "ALTER",
            "TRUNCATE", "CREATE", "REPLACE", "MERGE", "EXEC",
            "EXECUTE", "GRANT", "REVOKE", "COMMIT", "ROLLBACK"
    );

    public void validateSQL(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new SQLBlockedException("SQL query is empty!");
        }

        String upperSQL = sql.trim().toUpperCase();

        // Must start with SELECT
        if (!upperSQL.startsWith("SELECT")) {
            throw new SQLBlockedException(
                    "Only SELECT queries are allowed! " +
                            "Blocked query: " + sql);
        }

        // Check for blocked keywords
        for (String keyword : BLOCKED_KEYWORDS) {
            Pattern pattern = Pattern.compile(
                    "\\b" + keyword + "\\b",
                    Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(sql).find()) {
                throw new SQLBlockedException(
                        "Blocked keyword detected: " + keyword +
                                ". Only SELECT queries are allowed!");
            }
        }

        // Check for SQL injection patterns
        if (sql.contains(";") && sql.indexOf(";") < sql.length() - 1) {
            throw new SQLBlockedException(
                    "Multiple SQL statements are not allowed!");
        }
    }

    public boolean isSafe(String sql) {
        try {
            validateSQL(sql);
            return true;
        } catch (SQLBlockedException e) {
            return false;
        }
    }
}