package com.salesanalysis.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SaleRecord {
    private final String invoice;
    private final String stockCode;
    private final String description;
    private final int quantity;
    private final LocalDateTime invoiceDate;
    private final BigDecimal price;
    private final Long customerId;
    private final String country;

    public SaleRecord(String invoice,
                      String stockCode,
                      String description,
                      int quantity,
                      LocalDateTime invoiceDate,
                      BigDecimal price,
                      Long customerId,
                      String country) {
        this.invoice = invoice;
        this.stockCode = stockCode;
        this.description = description;
        this.quantity = quantity;
        this.invoiceDate = invoiceDate;
        this.price = price;
        this.customerId = customerId;
        this.country = country;
    }

    public String getInvoice() { return invoice; }

    public String getStockCode() { return stockCode; }

    public String getDescription() { return description; }

    public int getQuantity() { return quantity; }

    public LocalDateTime getInvoiceDate() { return invoiceDate; }

    public BigDecimal getPrice() { return price; }

    public Long getCustomerId() { return customerId; }

    public String getCountry() { return country; }

    public BigDecimal getLineTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
