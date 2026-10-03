package com.mobilestore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = System.getenv("JDBC_DATABASE_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email and password are required.");
            return;
        }

        boolean credentialsMatch;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT 1 FROM user_details WHERE email = ? AND password = ?")) {
                statement.setString(1, email);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    credentialsMatch = resultSet.next();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            getServletContext().log("Login database operation failed.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to process login.");
            return;
        }

        String destination = credentialsMatch ? "/welcome" : "/unauthorized.html";
        response.sendRedirect(request.getContextPath() + destination);
    }
}
