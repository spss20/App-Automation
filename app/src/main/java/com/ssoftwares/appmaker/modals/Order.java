package com.ssoftwares.appmaker.modals;

public class Order {

    private String outputUrl;
    private int orderId;
    private SubProduct subproduct;
    private Attachment config;
    private String orderName;
    private Attachment orderImage;

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

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Attachment getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(Attachment orderImage) {
        this.orderImage = orderImage;
    }
}
