package com.ssoftwares.appmaker.modals;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

    private String id;
    private String name;
    private String appCount;
    private Attachment image;
    private List<Product> products;

    public Category() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppCount() {
        return appCount;
    }

    public void setAppCount(String appCount) {
        this.appCount = appCount;
    }

    public Attachment getImage(){
        return image;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }
}

