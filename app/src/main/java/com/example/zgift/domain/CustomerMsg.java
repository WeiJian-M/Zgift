package com.example.zgift.domain;

public class CustomerMsg {

    private int customerId;
    private String customerName;
    private String customerMessage;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    @Override
    public String toString() {
        return "CustomerMsg{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerMessage='" + customerMessage + '\'' +
                '}';
    }
}
