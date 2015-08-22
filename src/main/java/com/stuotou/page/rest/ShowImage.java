package com.stuotuo.page.rest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.*;
import java.sql.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("serial")
@WebServlet("/showall")
public class ShowImage extends HttpServlet {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://127.0.0.1/tianqiw";

    public void init() throws ServletException {
        // Do required initialization
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("application/json");
        final ObjectMapper mapper = new ObjectMapper();
        JSONObject res = new JSONObject();
        JSONArray list = new JSONArray();

        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, "root", "stuotuo2");
            
            if (request.getParameterMap().containsKey("title")) {
                String title = request.getParameter("title");
                stmt = conn.prepareStatement("select title,image,url from WishList where title like ?;");
                stmt.setString(1, "%"+title+"%");
            } else {
                stmt = conn.prepareStatement("select title,image,url from WishList;");
            }
            ResultSet set = stmt.executeQuery();

            while(set.next()) {
                JSONObject obj = new JSONObject();
                String title = set.getString("title");
                String image = set.getString("image");
                String url = set.getString("url");
                obj.put("title", title);
                obj.put("image", image);
                obj.put("url", url);
                list.add(obj);
            }
            res.put("data", list);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println(mapper.writeValueAsString(res));
    }
  
    public void destroy() {
        // do nothing.
    }
}
