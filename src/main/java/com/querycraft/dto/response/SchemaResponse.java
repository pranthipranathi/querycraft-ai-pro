package com.querycraft.dto.response;

import java.util.List;
import java.util.Map;

public class SchemaResponse {

    private String tableName;
    private List<Map<String, String>> columns;
    private Integer columnCount;

    public SchemaResponse() {}

    public SchemaResponse(String tableName,
                          List<Map<String, String>> columns,
                          Integer columnCount) {
        this.tableName = tableName;
        this.columns = columns;
        this.columnCount = columnCount;
    }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public List<Map<String, String>> getColumns() { return columns; }
    public void setColumns(List<Map<String, String>> columns) { this.columns = columns; }

    public Integer getColumnCount() { return columnCount; }
    public void setColumnCount(Integer columnCount) { this.columnCount = columnCount; }
}