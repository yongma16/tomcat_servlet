package com.yma16.requset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("session")
public class SessionDemo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //session是服务器自动创建
        req.setCharacterEncoding("UTF-8");//统一采用utf-8

        resp.setCharacterEncoding("UTF-8");//设置服务端
        resp.setHeader("Content-Type","text/html;character=UTF-8");//设置header

        HttpSession session=req.getSession();//获取
        session.setAttribute("key","value");

        req.getRequestDispatcher("session.html").forward(req,resp);//转发重定向
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        super.doPost(req, resp);
        req.setCharacterEncoding("UTF-8");//统一采用utf-8

        resp.setCharacterEncoding("UTF-8");//设置服务端
        resp.setHeader("Content-Type","text/html;character=UTF-8");//设置header

        HttpSession session=req.getSession();//获取
        session.setAttribute("key","value");
        req.getRequestDispatcher("session.html").forward(req,resp);//转发重定向
    }
}
