package com.yma16.batabase;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;
//import org.apache.commons.dbutils.DbUtils;


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
            System.out.println("getconnection");
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
            System.out.println("begin");
            connection=getConnection();
            connection.setAutoCommit(false);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void commit(){
        Connection connection=null;
        try{
            System.out.println("commit");
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
            System.out.println("rollback……");
            connection=getConnection();
            connection.rollback();//回滚
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally{
            closeAll(connection,null,null);
        }
    }

    public static void closeAll(Connection connection, Statement statement, ResultSet resultset)
    {
        try{
            System.out.println("closeAll");
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
