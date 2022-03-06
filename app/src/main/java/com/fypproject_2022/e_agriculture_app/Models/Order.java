package com.fypproject_2022.e_agriculture_app.Models;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    String id;
    String customerId;
    String productId;
    String storeId;
    String dateTime;
    String status;
    int amount;

    public Order() {
    }

    public Order(String id, String customerId, String productId, String storeId, String dateTime, String status, int amount) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.storeId = storeId;
        this.dateTime = dateTime;
        this.status = status;
        this.amount = amount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
