package com.salesanalysis;

import com.salesanalysis.io.CsvSalesLoader;
import com.salesanalysis.model.SaleRecord;
import com.salesanalysis.service.SalesAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class SalesAnalysisServiceTest {

    private SalesAnalysisService service;

    @BeforeEach
    void setUp() throws IOException {
        CsvSalesLoader loader = new CsvSalesLoader();
        List<SaleRecord> records = loader.loadFromClasspath("sample_sales.csv");
        service = new SalesAnalysisService(records);
    }

    @Test
    void totalRevenue_shouldMatchExpected() {
        BigDecimal total = service.totalRevenue();
        assertEquals(new BigDecimal("57.50"), total);
    }

    @Test
    void revenueByCountry_shouldAggregateCorrectly() {
        Map<String, BigDecimal> byCountry = service.revenueByCountry();
        assertEquals(new BigDecimal("25.00"), byCountry.get("United Kingdom"));
        assertEquals(new BigDecimal("10.00"), byCountry.get("Germany"));
        assertEquals(new BigDecimal("22.50"), byCountry.get("France"));
    }

    @Test
    void topProductsByRevenue_shouldOrderCorrectly() {
        var top = service.topProductsByRevenue(1);
        assertFalse(top.isEmpty());
        assertEquals("Product A", top.get(0).getKey());
    }

    @Test
    void revenueByMonth_shouldGroupAllIntoDecember2009() {
        var byMonth = service.revenueByMonth();

        YearMonth december2009 = YearMonth.of(2009, 12);
        assertTrue(byMonth.containsKey(december2009));

        assertEquals(new BigDecimal("57.50"), byMonth.get(december2009));
        assertEquals(1, byMonth.size());
    }

    @Test
    void averageOrderValue_shouldBeCorrect() {
        BigDecimal avg = service.averageOrderValue();
        assertEquals(new BigDecimal("19.17"), avg);
    }

    @Test
    void topCustomerByRevenue_shouldReturnCustomer100() {
        var topCustomerOpt = service.topCustomerByRevenue();
        assertTrue(topCustomerOpt.isPresent());

        var topCustomer = topCustomerOpt.get();
        assertEquals(100L, topCustomer.getKey());
        assertEquals(new BigDecimal("47.50"), topCustomer.getValue());
    }
}
