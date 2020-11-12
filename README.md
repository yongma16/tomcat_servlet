# tomcat_servlet
### `实现登录注册`
#### 功能逻辑
传统的分为三层

 - Entity（写数据库实体类）
 - Dao（数据库表的增删改查）
 - Service（调用Dao实现登陆注册）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201112103818384.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4ODcwMTQ1,size_16,color_FFFFFF,t_70#pic_center)
细化的话还可以多一层requestmaping
cookie传递参数给前端界面

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201112104752938.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4ODcwMTQ1,size_16,color_FFFFFF,t_70#pic_center)
#### 配置mysql连接

```java
package com.yma16.batabase;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Dbutils {
    private static DruidDataSource ds;
    private static final ThreadLocal<Connection> THREAD_LOCAL=new ThreadLocal<>();

    static{
        Properties properties=new Properties();
        InputStream inputStream= DbUtils.class.getResourceAsStream("/mysqldatabase.properties");//mysql连接
        try{
            properties.load(inputStream);
            ds=(DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        }catch (IOException e)
        {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        Connection connection=THREAD_LOCAL.get();
        try{
            if(connection==null)
            {
                connection=ds.getConnection();
                THREAD_LOCAL.set(connection);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return connection;
    }
    public static void begin(){
        Connection connection=null;
        try{
            connection=getConnection();
            connection.setAutoCommit(true);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void commit(){
        Connection connection=null;
        try{
            connection=getConnection();
            connection.commit();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            closeAll(connection,null,null);
        }
    }

    public static void rollback()
    {
        Connection connection=null;
        try
        {
            connection=getConnection();
            connection.rollback();//回滚
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally {
            closeAll(connection,null,null);
        }
    }

    public static void closeAll(Connection connection, Statement statement, ResultSet resultset)
    {
        try{
            if(resultset!=null){
                resultset.close();
            }
            if(statement!=null)
            {
                statement.close();
            }
            if(connection!=null)
            {
                connection.close();
                THREAD_LOCAL.remove();
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}

```
#### Entity
实体

```java
package com.yma16.entity;

public class User {
    private String username,userpassword;//对应数据库user

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUsername() {
        return username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userpassword='" + userpassword + '\'' +
                '}';
    }
}

```

#### Dao
数据增删改查接口

```java
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
```
接口实现类

```java
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
            User user=queryRunner.query(Dbutils.getConnection(),"insert into user (username,password)values(?,?)",new BeanHandler<User>(User.class));
            return user;//user
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User select(String username) {
        System.out.println("sql根据username查询");
        try{
            User user=queryRunner.query(Dbutils.getConnection(),"select * from user where username=?",new BeanHandler<User>(User.class));
            return user;//user
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(String username) {
        System.out.println("sql根据username删除");
        try{
            User user=queryRunner.query(Dbutils.getConnection(),"delete from user where username=?",new BeanHandler<User>(User.class));
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

```
#### Service
登陆注册接口

```java
package com.yma16.service;

import com.yma16.entity.User;

import java.util.List;

public interface UserService {
    public User register(String name,String password);//注册
    public User login(String name, String password);//登陆
    public List<User> showAllAdmin();//查询
}

```
接口实现

```java
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

```
#### servlet层
登陆调用service的login

```java
User user=userService.login(username,password);//调用sql查询
```

注册调用service的register

```java
User user=userService.register(username,password);//注册
```

