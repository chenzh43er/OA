package com.shop.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Product",urlPatterns = "/product")
public class Product extends HttpServlet {
    @Override
    public void init() throws ServletException {
        System.out.println("start");
        super.init();
    }

    @Override
    public void destroy() {
        System.out.println("destory");
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
