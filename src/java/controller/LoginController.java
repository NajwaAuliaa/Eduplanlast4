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
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Fetch user from the database using email
            User user = userDAO.getUserByEmail(email);

            // Check if the user exists and the password matches
            if (user != null && user.getPassword().equals(password)) {
                // Store user in session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);       // Store the entire user object
                session.setAttribute("userId", user.getId()); // Store userId for convenience

                // Redirect to the ScheduleController to display the schedule
                response.sendRedirect("schedule");
            } else {
                // Redirect back to login with an error message if credentials are invalid
                response.sendRedirect("invalidLogin.jsp?error=Invalid credentials");
            }
        } catch (Exception e) {
            // Handle exceptions and propagate as ServletException
            throw new ServletException("Error during login", e);
        }
    }
}
