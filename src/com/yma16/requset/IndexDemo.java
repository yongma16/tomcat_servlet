package com.yma16.requset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//注册
@WebServlet("/index")
public class IndexDemo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //重写get方法，tomcat7以上的版本有个收参的问题
        Cookie cookie=new Cookie("name","yma16");
        cookie.setPath("/tomcat_servlet_war_exploded/register");//设置路径
        cookie.setMaxAge(20);//生命周期（0表示直到浏览器关闭，大于0代表毫秒数，小于0代表浏览器内部存储）
        resp.addCookie(cookie);//添加cookies传递
        req.getRequestDispatcher("cookies.html").forward(req,resp);//转发重定向
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        super.doPost(req, resp);
        req.setCharacterEncoding("UTF-8");//统一采用utf-8

        resp.setCharacterEncoding("UTF-8");//设置服务端
        resp.setHeader("Content-Type","text/html;character=UTF-8");//设置header
        //同时设置response
        //resp.setContentType("text/html;character=UTF-8");
        PrintWriter printWriter=resp.getWriter();
        //创建cookies
        Cookie cookie=new Cookie("name","yma16");
        cookie.setPath("/tomcat_servlet_war_exploded/register");//设置路径
        cookie.setMaxAge(20);//生命周期（0表示直到浏览器关闭，大于0代表毫秒数，小于0代表浏览器内部存储）
        resp.addCookie(cookie);//添加cookies传递
        printWriter.println("注册成功！");
    }
}
