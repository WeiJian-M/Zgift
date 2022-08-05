package com.example.zgift.domain;

public class Courier {

    private int courierId;
    private String courierName;

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    @Override
    public String toString() {
        return "Courier{" +
                "courierId=" + courierId +
                ", courierName='" + courierName + '\'' +
                '}';
    }
}
