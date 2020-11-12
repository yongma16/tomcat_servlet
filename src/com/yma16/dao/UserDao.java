package com.yma16.dao;

import com.yma16.entity.User;

import java.util.List;

public interface UserDao {//功能接口
    public User insert(String username,String password);//添加数据
    public User select(String username);//查询
    public int delete(String username);//删除数据
    public int update(User user);//
    public List<User> selectAll();//查找

}
