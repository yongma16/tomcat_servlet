package com.yma16.requset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//注册
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //重写get方法，tomcat7以上的版本有个收参的问题
        String username=req.getParameter("username");//get 用户名
        String userpassword=req.getParameter("password");//get 密码
        System.out.println("提交的数据"+username+"\t"+userpassword);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        req.setCharacterEncoding("UTF-8");//统一采用utf-8
        String username=req.getParameter("username");//get 用户名
        String userpassword=req.getParameter("password");//get 密码
        System.out.println("提交的数据"+username+"\t"+userpassword);

        resp.setCharacterEncoding("UTF-8");//设置服务端
        resp.setHeader("Content-Type","text/html;character=UTF-8");//设置header
        //同时设置response
        //resp.setContentType("text/html;character=UTF-8");
        PrintWriter printWriter=resp.getWriter();
        printWriter.println("注册成功！");
    }
}
