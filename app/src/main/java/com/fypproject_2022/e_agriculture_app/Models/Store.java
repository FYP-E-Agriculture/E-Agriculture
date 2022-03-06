package com.fypproject_2022.e_agriculture_app.Models;

import java.io.Serializable;
import java.util.List;

public class Store implements Serializable {
    String id;
    String vendorId;
    String image;
    String name;
    String city;
    String openingYear;
    String phone;
    String address;
    List<Product> productList;

    public Store() {
    }

    public Store(String id, String vendorId, String image, String name, String city, String openingYear, String phone, String address, List<Product> productList) {
        this.id = id;
        this.vendorId = vendorId;
        this.image = image;
        this.name = name;
        this.city = city;
        this.openingYear = openingYear;
        this.phone = phone;
        this.address = address;
        this.productList = productList;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpeningYear() {
        return openingYear;
    }

    public void setOpeningYear(String openingYear) {
        this.openingYear = openingYear;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
