package com.yma16.entity;

public class User {
    private String username,password;//对应数据库user

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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                "password='" + password + '\'' +
                '}';
    }
}
