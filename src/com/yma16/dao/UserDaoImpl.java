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
    public User insert(String username,String password) {
        System.out.println("sql插入数据");
        try{
//            String sql="select username,password from user where username=? and password=?;";
            String sql="insert into user values(?,?)";
            queryRunner.query(Dbutils.getConnection(),sql,new BeanHandler<User>(User.class),username,password);//测试
            System.out.println("loading 插入数据");
            User user=queryRunner.query(Dbutils.getConnection(),sql,new BeanHandler<User>(User.class),username,password);
            return user;//user
        }catch (SQLException e)
        {
            System.out.println("error");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User select(String username) {
        System.out.println("sql根据username查询");
        try{
            String sql="select username,password from user where username=?";
            User user=queryRunner.query(Dbutils.getConnection(),sql,new BeanHandler<User>(User.class),username);
            return user;//user
        }catch (SQLException e)
        {
            System.out.println("error");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(String username) {
        System.out.println("sql根据username删除");
        try{
            User user=queryRunner.query(Dbutils.getConnection(),"delete from user where username=?;",new BeanHandler<User>(User.class),username);
            return 1;//删除成功
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(User user) {
//      更新数据

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
