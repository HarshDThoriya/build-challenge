package com.salesanalysis.io;

import com.salesanalysis.model.SaleRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CsvSalesLoader {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("M/d/yyyy H:mm");

    public List<SaleRecord> loadFromClasspath(String resourcePath) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines()
                        .skip(1) // header
                        .map(this::parseLineSafely)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        }
    }

    private SaleRecord parseLineSafely(String line) {
        try {
            return parseLine(line);
        } catch (Exception e) {
            return null;
        }
    }

    private SaleRecord parseLine(String line) {
        String[] parts = parseCsvLine(line);

        if (parts.length < 8) {
            throw new IllegalArgumentException("Expected at least 8 columns, got " + parts.length);
        }

        String invoice = parts[0].trim();
        String stockCode = parts[1].trim();
        String description = parts[2].trim();
        int quantity = Integer.parseInt(parts[3].trim());
        LocalDateTime invoiceDate = LocalDateTime.parse(parts[4].trim(), DATE_FORMATTER);
        BigDecimal price = new BigDecimal(parts[5].trim());
        Long customerId = parts[6].trim().isEmpty() ? null : Long.parseLong(parts[6].trim());
        String country = parts[7].trim();

        return new SaleRecord(invoice, stockCode, description, quantity, invoiceDate, price, customerId, country);
    }

    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            // added extra checks because of format of csv file
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    current.append('"');
                    i++; // skip next quote
                } else {
                    // Toggle quote mode
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Field separator
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        result.add(current.toString());

        return result.toArray(new String[0]);
    }
}
