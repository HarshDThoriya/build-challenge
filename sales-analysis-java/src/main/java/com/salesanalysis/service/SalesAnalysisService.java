package com.salesanalysis.service;

import com.salesanalysis.model.SaleRecord;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Pure functions that aggregate sales rows into KPIs.
 * Keep these side-effect free so they’re easy to test.
 *
 * Methods covered by unit tests:
 * - totalRevenue
 * - revenueByMonth
 * - topProductsByRevenue
 * - topCountriesByRevenue
 * - averageOrderValue
 * - topCustomersByRevenue
 */
public class SalesAnalysisService {

    private final List<SaleRecord> records;

    public SalesAnalysisService(List<SaleRecord> records) {
        this.records = records;
    }

    public BigDecimal totalRevenue() {
        return records.stream()
                .map(SaleRecord::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, BigDecimal> revenueByCountry() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SaleRecord::getCountry,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                SaleRecord::getLineTotal,
                                BigDecimal::add
                        )
                ));
    }

    public List<Map.Entry<String, BigDecimal>> topProductsByRevenue(int limit) {
        Map<String, BigDecimal> revenueByProduct = records.stream()
                .collect(Collectors.groupingBy(
                        SaleRecord::getDescription,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                SaleRecord::getLineTotal,
                                BigDecimal::add
                        )
                ));

        return revenueByProduct.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<YearMonth, BigDecimal> revenueByMonth() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        r -> YearMonth.from(r.getInvoiceDate()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                SaleRecord::getLineTotal,
                                BigDecimal::add
                        )
                ));
    }

    public BigDecimal averageOrderValue() {
        Map<String, BigDecimal> orderTotals = records.stream()
                .collect(Collectors.groupingBy(
                        SaleRecord::getInvoice,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                SaleRecord::getLineTotal,
                                BigDecimal::add
                        )
                ));

        return orderTotals.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(orderTotals.size()), 2, BigDecimal.ROUND_HALF_UP);
    }

    public Optional<Map.Entry<Long, BigDecimal>> topCustomerByRevenue() {
        Map<Long, BigDecimal> revenueByCustomer = records.stream()
                .filter(r -> r.getCustomerId() != null)
                .collect(Collectors.groupingBy(
                        SaleRecord::getCustomerId,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                SaleRecord::getLineTotal,
                                BigDecimal::add
                        )
                ));

        return revenueByCustomer.entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }
}
