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
        response.getWriter().print("response test chinese 成功！");//中文乱码
        request.setCharacterEncoding("UTF-8");
    }
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
    {
        doGet( request,response);
    }
}
