package com.example.auth;

public class User {

    String name, email, fireBaseUID;

    public User(){}

    public User(String name, String email, String fireBaseUID) {
        this.name = name;
        this.email = email;
        this.fireBaseUID = fireBaseUID;
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

    public String getFireBaseUID() {
        return fireBaseUID;
    }

    public void setFireBaseUID(String fireBaseUID) {
        this.fireBaseUID = fireBaseUID;
    }
}
