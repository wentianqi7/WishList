package com.stuotuo.page.rest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.*;
import java.sql.*;


@SuppressWarnings("serial")
@WebServlet("/wishlist")
public class MiniSite extends HttpServlet {
    static String message;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://127.0.0.1/tianqiw";

    public void init() throws ServletException {
        // Do required initialization
        message = "Insert failed";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");

        Connection conn = null;
        PreparedStatement check = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, "root", "stuotuo2");

            check = conn.prepareStatement("select id from WishList where title=?;");
            check.setString(1, request.getParameter("title"));
            ResultSet set = check.executeQuery();
            if (!set.next()) {
                stmt = conn.prepareStatement("insert into WishList (title, image, url) values (?,?,?);");
                stmt.setString(1, request.getParameter("title"));
                stmt.setString(2, request.getParameter("image"));
                stmt.setString(3, request.getParameter("url"));
                stmt.executeUpdate();
                message = "Add successful";
            } else {
                message = "Item already exists";
            }

            check.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (check != null) check.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println(message);
    }
  
    public void destroy() {
        // do nothing.
    }
}
