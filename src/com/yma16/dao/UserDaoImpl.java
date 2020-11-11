package com.yma16.dao;

import com.yma16.batabase.Dbutils;
import com.yma16.entity.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao{//接口实现类

    private QueryRunner queryRunner=new QueryRunner();

    @Override
    public int insert(User user) {
        return 0;
    }

    @Override
    public User select(String username) {
        try{
            User user=queryRunner.query(Dbutils.getConnection(),"select * from where username=?",new BeanHandler<User>(User.class));
            return user;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(String username) {
        return 0;
    }

    @Override
    public int update(User user) {
        return 0;
    }

    @Override
    public List<User> selectAll() {
        try{
            List<User> users=queryRunner.query(Dbutils.getConnection(),"select * from user",new BeanListHandler<User>(User.class));
            return users;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}