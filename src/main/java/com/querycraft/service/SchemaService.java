package com.querycraft.service;

import com.querycraft.dto.response.SchemaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchemaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getAllTableNames() {
        String sql = """
                SELECT TABLE_NAME 
                FROM INFORMATION_SCHEMA.TABLES 
                WHERE TABLE_SCHEMA = DATABASE()
                AND TABLE_TYPE = 'BASE TABLE'
                ORDER BY TABLE_NAME
                """;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public SchemaResponse getTableSchema(String tableName) {
        String sql = """
                SELECT COLUMN_NAME, DATA_TYPE, 
                       IS_NULLABLE, COLUMN_KEY, COLUMN_DEFAULT
                FROM INFORMATION_SCHEMA.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                AND TABLE_NAME = ?
                ORDER BY ORDINAL_POSITION
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, tableName);
        List<Map<String, String>> columns = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Map<String, String> column = new HashMap<>();
            column.put("name", String.valueOf(row.get("COLUMN_NAME")));
            column.put("type", String.valueOf(row.get("DATA_TYPE")));
            column.put("nullable", String.valueOf(row.get("IS_NULLABLE")));
            column.put("key", String.valueOf(row.get("COLUMN_KEY")));
            columns.add(column);
        }

        return new SchemaResponse(tableName, columns, columns.size());
    }

    public String getSchemaAsString() {
        StringBuilder schema = new StringBuilder();
        List<String> tables = getAllTableNames();

        for (String tableName : tables) {
            SchemaResponse tableSchema = getTableSchema(tableName);
            schema.append("Table: ").append(tableName).append("\n");
            schema.append("Columns:\n");
            for (Map<String, String> col : tableSchema.getColumns()) {
                schema.append("  - ")
                        .append(col.get("name"))
                        .append(" (")
                        .append(col.get("type"))
                        .append(")\n");
            }
            schema.append("\n");
        }

        return schema.toString();
    }
}