package com.example.appgym.model;

public class Account {
    String username,password,phone,question;

    public Account() {
    }

    public Account(String username, String password, String phone, String question) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.question = question;
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
