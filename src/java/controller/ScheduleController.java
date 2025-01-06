/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.IActivityDAO;
import dao.ActivityDAO;
import model.Activity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/schedule")
public class ScheduleController extends HttpServlet {
    private ActivityDAO activityDAO = new ActivityDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp?error=Please log in to view your schedule");
            return;
        }

        try {
            // Fetch activities for the logged-in user
            List<Activity> activities = activityDAO.getActivitiesByUserId(userId);

            // Set activities in the request scope
            request.setAttribute("activities", activities);

            // Forward to the schedule.jsp page
            request.getRequestDispatcher("schedule.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving activities", e);
        }
    }
}
