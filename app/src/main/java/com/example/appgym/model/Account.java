package com.example.appgym.model;

public class Account {
    String auth,username,password,phone,question;

    public Account() {
    }

    public Account(String auth, String username, String password, String phone, String question) {
        this.auth = auth;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.question = question;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
