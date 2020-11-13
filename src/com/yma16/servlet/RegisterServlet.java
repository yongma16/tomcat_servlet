package com.yma16.servlet;

import com.yma16.entity.User;
import com.yma16.service.UserService;
import com.yma16.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/doregister")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        System.out.println("get");
        req.setCharacterEncoding("UTF-8");//统一UTF-8

        //get参数
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        System.out.println(username+"\t"+password);
        resp.setContentType("text/html;charaset=UTF-8");
        //调用登陆业务功能逻辑
        UserService userService=new UserServiceImpl();//调用接口功能
        User user=userService.register(username,password);//注册
        PrintWriter printWriter=resp.getWriter();
        if(user!=null){
//            printWriter.println("通过jdbc登陆成功！");
            resp.sendRedirect("register_right.html");//登陆成功重定向
        }
        else{
            printWriter.println("通过jdbc登陆失败");//未定义跳转
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post");
        req.setCharacterEncoding("UTF-8");//统一UTF-8

        //get参数
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        System.out.println(username+"\t"+password);
        resp.setContentType("text/html;charaset=UTF-8");
        //调用登陆业务功能逻辑
        UserService userService=new UserServiceImpl();//调用接口功能
        User user=userService.register(username,password);//调用sql查询
        PrintWriter printWriter=resp.getWriter();
        if(user!=null){
//            printWriter.println("通过jdbc登陆成功！");
            resp.sendRedirect("register_right.html");//登陆成功重定向
        }
        else{
            printWriter.println("通过jdbc登陆失败");//未定义跳转
        }
    }
}
