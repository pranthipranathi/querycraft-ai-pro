package com.querycraft.service;

import com.querycraft.entity.QueryHistory;
import com.querycraft.exception.ResourceNotFoundException;
import com.querycraft.repository.QueryHistoryRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Service
public class ExportService {

    @Autowired
    private QueryHistoryRepository queryHistoryRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public byte[] exportAsCSV(Long historyId) {
        try {
            QueryHistory history = queryHistoryRepository.findById(historyId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "History not found: " + historyId));

            String sql = history.getGeneratedSql();
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

            StringWriter stringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(stringWriter);

            if (!results.isEmpty()) {
                String[] headers = results.get(0).keySet()
                        .toArray(new String[0]);
                csvWriter.writeNext(headers);

                for (Map<String, Object> row : results) {
                    String[] values = row.values().stream()
                            .map(v -> v != null ? v.toString() : "")
                            .toArray(String[]::new);
                    csvWriter.writeNext(values);
                }
            }

            csvWriter.close();
            return stringWriter.toString().getBytes();

        } catch (Exception e) {
            throw new RuntimeException("CSV export failed: " + e.getMessage());
        }
    }

    public byte[] exportAsPDF(Long historyId) {
        try {
            QueryHistory history = queryHistoryRepository.findById(historyId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "History not found: " + historyId));

            String sql = history.getGeneratedSql();
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16,
                    Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10,
                    Font.BOLD, BaseColor.WHITE);
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 9);

            Paragraph title = new Paragraph(
                    "QueryCraft AI Pro — Query Results", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);
            document.add(title);

            Paragraph query = new Paragraph(
                    "Query: " + history.getNaturalLanguageQuery(),
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
            query.setSpacingAfter(5);
            document.add(query);

            Paragraph sqlPara = new Paragraph(
                    "SQL: " + sql,
                    new Font(Font.FontFamily.HELVETICA, 9));
            sqlPara.setSpacingAfter(15);
            document.add(sqlPara);

            if (!results.isEmpty()) {
                int cols = results.get(0).size();
                PdfPTable table = new PdfPTable(cols);
                table.setWidthPercentage(100);

                for (String header : results.get(0).keySet()) {
                    PdfPCell cell = new PdfPCell(
                            new Phrase(header, headerFont));
                    cell.setBackgroundColor(new BaseColor(26, 26, 46));
                    cell.setPadding(8);
                    table.addCell(cell);
                }

                boolean alternate = false;
                for (Map<String, Object> row : results) {
                    for (Object value : row.values()) {
                        PdfPCell cell = new PdfPCell(
                                new Phrase(value != null ?
                                        value.toString() : "", cellFont));
                        cell.setPadding(6);
                        if (alternate) {
                            cell.setBackgroundColor(
                                    new BaseColor(245, 245, 245));
                        }
                        table.addCell(cell);
                    }
                    alternate = !alternate;
                }

                document.add(table);
            }

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF export failed: " + e.getMessage());
        }
    }
}