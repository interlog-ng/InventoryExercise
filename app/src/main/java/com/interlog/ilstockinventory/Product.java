package com.interlog.ilstockinventory;

public class Product {

    private int id;
    private String productName;
    private String locationAddr;
    private String customerName;
    private String reportingDate;

    public Product(int id, String productName, String locationAddr, String customerName, String reportingDate) {
        this.id = id;
        this.productName = productName;
        this.locationAddr = locationAddr;
        this.customerName = customerName;
        this.reportingDate = reportingDate;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getLocationAddr() {
        return locationAddr;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getReportingDate() {
        return reportingDate;
    }
}
