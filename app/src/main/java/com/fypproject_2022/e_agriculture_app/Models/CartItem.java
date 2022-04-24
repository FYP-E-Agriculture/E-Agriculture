package com.fypproject_2022.e_agriculture_app.Models;

public class CartItem {
    String id;
    String productId;
    String customerId;
    String storeId;

    public CartItem() {
    }

    public CartItem(String id, String productId, String customerId, String storeId) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.storeId = storeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
