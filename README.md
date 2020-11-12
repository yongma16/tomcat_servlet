@[TOC](servlet结合jdbc)
### 效果展示
 **登录**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201112223530947.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4ODcwMTQ1,size_16,color_FFFFFF,t_70#pic_center)
**注册**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201112223800688.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4ODcwMTQ1,size_16,color_FFFFFF,t_70#pic_center)


### Servlet接口
接口包括五个方法
 - `void init`(ServletConfig config)
 - `ServletConfig getServletConfig`()
 - `void service`(ServletRequest request, ServletResponse response)
 - `String getServletInfo`()
 - `void destroy`()
#### GenericServlet抽象类
实现Servlet、ServletConfig、Serializable接口，需重写service方法。
#### HttpServlet类
继承GenericServlet基础上的扩展，子类必须重写一个方法。
一般只需重写doGet和doPost方法。
 - doGet
 - doPost
 - doPut
 - doDelete
 ### Servlet创建方式
 #### 实现Servlet接口
 直接继承原生的Servlet，较为麻烦

```java
package com.yma16;
import javax.servlet.*;
import java.io.IOException;
public class MyServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
    }
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("测试1");
    }
    @Override
    public String getServletInfo() {
        return null;
    }
    @Override
    public void destroy() {
    }
}
```

 #### 继承HttpServlet
 大部分开发人员推荐的创建方式，一般重写
 
```java
@WebServlet("/hello")//value urlpattern
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.getWriter().print("测试！");
    }
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
    {
        doGet( request,response);
    }
}
```
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
实体对应数据库表user
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201112214631364.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4ODcwMTQ1,size_16,color_FFFFFF,t_70#pic_center)


```java
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
        try{
            String sql="insert into user values(?,?)";
            User user=queryRunner.query(Dbutils.getConnection(),sql,new BeanHandler<User>(User.class),username,password);
            return user;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User select(String username) {
        try{
            String sql="select * from where username=?";
            User user=queryRunner.query(Dbutils.getConnection(),sql,new BeanHandler<User>(User.class),username);
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
    public User register(String username, String password) {
        User result=null;
        try{
            Dbutils.begin();
            User user=userdao.insert(username,password);//插入用户名和密码
            if(user!=null){
                if(user.getPassword().equals(password))
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
    public User login(String username, String password) {
        User result=null;
        System.out.println("loading login");
        try{
            Dbutils.begin();
            User user=userdao.select(username);//名字查询
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


```
#### servlet调用service
登陆调用service的login

```java
User user=userService.login(username,password);//调用sql查询
```

注册调用service的register

```java
User user=userService.register(username,password);//注册
```
**end**
代码：[我的仓库](https://github.com/yongma16?tab=repositories)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201112103818384.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4ODcwMTQ1,size_16,color_FFFFFF,t_70#pic_center)
