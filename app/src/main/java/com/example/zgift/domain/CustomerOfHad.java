package com.example.zgift.domain;

public class CustomerOfHad {

    private String customerName;
    private String courierName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    @Override
    public String toString() {
        return "CustomerOfHad{" +
                "customerName='" + customerName + '\'' +
                ", courierName='" + courierName + '\'' +
                '}';
    }
}
