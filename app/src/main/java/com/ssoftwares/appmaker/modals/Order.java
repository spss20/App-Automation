package com.ssoftwares.appmaker.modals;

public class Order {

    private String outputUrl;
    private int orderId;
    private SubProduct subproduct;
    private Attachment config;

    public String getOutputUrl() {
        return outputUrl;
    }

    public void setOutputUrl(String outputUrl) {
        this.outputUrl = outputUrl;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public SubProduct getSubproduct() {
        return subproduct;
    }

    public void setSubproduct(SubProduct subproduct) {
        this.subproduct = subproduct;
    }

    public Attachment getConfig() {
        return config;
    }

    public void setConfig(Attachment config) {
        this.config = config;
    }
}
