package com.yma16.service;

import com.yma16.batabase.Dbutils;
import com.yma16.dao.UserDao;
import com.yma16.dao.UserDaoImpl;
import com.yma16.entity.User;

import java.util.List;

public class UserServiceImpl implements UserService{//实现service接口
    private UserDao userdao=new UserDaoImpl();//user接口功能
    @Override
    public User register(String name, String password) {
        User result=null;
        System.out.println("loading register");
        try{
            Dbutils.begin();
            User user=userdao.insert(name,password);//insert添加数据
            System.out.println(user);//数据添加结果
            if(user!=null){
                result=user;//数据添加成功则传递
            }
            Dbutils.commit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;//返回
    }

    @Override
    public User login(String name, String password) {
        User result=null;
        System.out.println("loading login");
        try{
            Dbutils.begin();
            User user=userdao.select(name);//名字查询
            System.out.println(user);//查询的结果
            if(user!=null){
                if(user.getPassword().equals(password))
                {
                    result=user;//查询存在则给result(对比)
                }
            }
            Dbutils.commit();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<User> showAllAdmin() {
        List<User> users=null;
        try{
            Dbutils.begin();
            users=userdao.selectAll();//查询所用user
            Dbutils.commit();
        }catch (Exception e)
        {
            Dbutils.rollback();
            e.printStackTrace();
        }
        return users;
    }
}
