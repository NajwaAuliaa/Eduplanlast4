/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author ASUS
 */
import dao.UserDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import util.DatabaseConnection;

@WebServlet("/signup")
public class SignupController extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            if (userDAO.emailExists(email)) {
                request.setAttribute("error", "Email sudah terdaftar. Silakan gunakan email lain.");
                request.getRequestDispatcher("emailExist.jsp").forward(request, response);
            } else {
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);  // In real applications, hash the password securely

                userDAO.addUser(user);
                response.sendRedirect("login.jsp");  // Redirect to login page after successful signup
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Terjadi kesalahan. Silakan coba lagi.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }
}
