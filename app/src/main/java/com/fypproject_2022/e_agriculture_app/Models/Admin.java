package com.fypproject_2022.e_agriculture_app.Models;

import java.io.Serializable;

public class Admin implements Serializable {
    String name;
    String username;
    String email;
    String image;
    String password;

    public Admin() {
    }

    public Admin(String name, String username, String email, String image, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.image = image;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
