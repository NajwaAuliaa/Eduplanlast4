/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author ASUS
 */

import dao.IActivityDAO;
import dao.ActivityDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteActivity")
public class DeleteActivityController extends HttpServlet {
    private final IActivityDAO activityDAO = new ActivityDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp?error=Please log in to delete activities");
            return;
        }

        try {
            // Retrieve activity ID from the request
            int activityId = Integer.parseInt(request.getParameter("activityId"));

            // Delete the activity
            activityDAO.deleteActivity(activityId);

            // Redirect to the schedule page
            response.sendRedirect("schedule");
        } catch (NumberFormatException e) {
            response.sendRedirect("schedule?error=Invalid activity ID");
        } catch (SQLException e) {
            throw new ServletException("Error deleting activity", e);
        }
    }
}
