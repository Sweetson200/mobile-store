package com.mobilestore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String url = System.getenv("JDBC_DATABASE_URL");
    private static final String user = System.getenv("DB_USER");
    private static final String dbPassword = System.getenv("DB_PASSWORD");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name == null || name.isBlank() || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Name, email, and password are required.");
            return;
        }

        if (password.length() < 8) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password must be at least 8 characters.");
            return;
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        try(Connection connection = DriverManager.getConnection(url, user, dbPassword)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from user_details where email='"+email+"'");
            if(resultSet.next()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email already exists.");
                return;
            }else{
                statement.executeUpdate("insert into user_details(name,email,password) values('"+name+"','"+email+"','"+password+"')");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed.");
            return;
        }   
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
    catch(Exception e){
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }
}
}
