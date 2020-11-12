package com.yma16;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")             //value urlpattern
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String s="测试中文是否通过";
//        request.setCharacterEncoding("UTF-8");
        response.getWriter().print("response test chinese 失败"+s);//中文乱码决解问题

    }
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
    {
//        doGet( request,response);
        request.setCharacterEncoding("UTF-8");//统一采用utf-8
    }
}
