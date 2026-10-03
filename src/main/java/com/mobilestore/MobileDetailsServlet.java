package com.mobilestore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MobileDetailsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = System.getenv("JDBC_DATABASE_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Map<String, String>> mobiles = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    Statement statement = connection.createStatement();
                    ResultSet results = statement.executeQuery(
                            "SELECT mobile_id, brand, model_name, color, storage_gb, ram_gb, price, "
                                    + "stock_quantity, image_url, description "
                                    + "FROM mobile_details WHERE COALESCE(is_active, 1) = 1 "
                                    + "ORDER BY brand, model_name, storage_gb")) {
                while (results.next()) {
                    int stockQuantity = results.getInt("stock_quantity");
                    if (results.wasNull()) {
                        stockQuantity = 0;
                    }

                    Map<String, String> mobile = new LinkedHashMap<>();
                    mobile.put("mobileId", results.getString("mobile_id"));
                    mobile.put("brand", results.getString("brand"));
                    mobile.put("modelName", results.getString("model_name"));
                    mobile.put("color", results.getString("color"));
                    mobile.put("storageGb", results.getString("storage_gb"));
                    mobile.put("ramGb", results.getString("ram_gb"));
                    mobile.put("price", results.getBigDecimal("price").toPlainString());
                    mobile.put("stockQuantity", Integer.toString(stockQuantity));
                    mobile.put("imageUrl", safeImageUrl(results.getString("image_url")));
                    mobile.put("description", results.getString("description"));
                    mobiles.add(mobile);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            getServletContext().log("Could not load mobile details.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load mobile details.");
            return;
        }

        request.setAttribute("mobiles", mobiles);
        request.getRequestDispatcher("/WEB-INF/welcome.jsp").forward(request, response);
    }

    private String safeImageUrl(String imageUrl) {
        if (imageUrl == null) {
            return "";
        }

        String candidate = imageUrl.trim();
        if (candidate.startsWith("https://") || candidate.startsWith("http://")
                || (candidate.startsWith("/") && !candidate.startsWith("//"))) {
            return candidate;
        }
        return "";
    }
}
