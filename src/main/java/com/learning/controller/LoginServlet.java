package com.learning.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.learning.entities.UserDetail;
import com.learning.repository.UserDetailRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

   @Autowired
   private UserDetailRepository  userDetailRepository;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login.html?error=invalid");
            return;
        }

        String normalizedEmail = email.trim();
        UserDetail user = userDetailRepository.findByEmailAndPassword(normalizedEmail, password);
        boolean authenticated = user != null;

        if (!authenticated) {
            response.sendRedirect(request.getContextPath() + "/login.html?error=invalid");
            return;
        }

        HttpSession existingSession = request.getSession(false);
        if (existingSession != null) {
            existingSession.invalidate();
        }
        request.getSession(true).setAttribute("userEmail", normalizedEmail);
        response.sendRedirect(request.getContextPath() + "/login.html?success=true");
    }
}
