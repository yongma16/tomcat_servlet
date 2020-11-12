package com.yma16.service;

import com.yma16.entity.User;

import java.util.List;

public interface UserService {
    public User register(String name,String password);//注册
    public User login(String name, String password);//登陆
    public List<User> showAllAdmin();//查询
}
