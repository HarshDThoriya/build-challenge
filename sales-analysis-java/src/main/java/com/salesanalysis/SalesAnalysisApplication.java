package com.salesanalysis;

import com.salesanalysis.io.CsvSalesLoader;
import com.salesanalysis.model.SaleRecord;
import com.salesanalysis.service.SalesAnalysisService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * Entry point for Assignment 2 (Sales Analysis).
 * Reads the Online Retail II CSV from resources and prints:
 * - Total revenue
 * - Top-10 countries by revenue
 * - Top-10 products by revenue
 * - Revenue by month
 * - Average Order Value
 * - Top customer by revenue
 *
 * Adjust the dataset path or parameters here as needed.
 */
public class SalesAnalysisApplication {

    public static void main(String[] args) throws IOException {
        CsvSalesLoader loader = new CsvSalesLoader();
        List<SaleRecord> records = loader.loadFromClasspath("data/online_retail_II.csv");

        SalesAnalysisService service = new SalesAnalysisService(records);

        BigDecimal totalRevenue = service.totalRevenue();
        System.out.println("=== Sales Analysis Results ===");
        System.out.println("Total Revenue: " + totalRevenue);

        System.out.println("\nTop 10 Countries by Revenue:");
        service.revenueByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(10)
                .forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));

        System.out.println("\nTop 10 Products by Revenue:");
        service.topProductsByRevenue(10)
                .forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));

        System.out.println("\nRevenue by Month:");
        service.revenueByMonth().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> {
                    YearMonth ym = e.getKey();
                    System.out.println(ym + " = " + e.getValue());
                });

        System.out.println("\nAverage Order Value: " + service.averageOrderValue());

        service.topCustomerByRevenue().ifPresent(entry ->
                System.out.println("\nTop Customer by Revenue: " + entry.getKey()
                        + " with " + entry.getValue())
        );
    }
}
