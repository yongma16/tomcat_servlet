package com.yma16.service;

import com.yma16.batabase.Dbutils;
import com.yma16.dao.UserDao;
import com.yma16.dao.UserDaoImpl;
import com.yma16.entity.User;

import java.util.List;

public class UserServiceImpl implements UserService{//实现service接口
    private UserDao userdao=new UserDaoImpl();//user接口功能

    @Override
    public User login(String name, String password) {
        User result=null;
        try{
            Dbutils.begin();
            User user=userdao.select(name);//名字查询
            if(user!=null){
                if(user.getUserpassword().equals(password))
                {
                    result=user;//查询存在则给result
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
