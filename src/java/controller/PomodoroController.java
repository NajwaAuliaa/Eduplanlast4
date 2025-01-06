package controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/pomodoroservlet")
public class PomodoroController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        // Initialize the timer
        String studyTimeInput = request.getParameter("study-time");
        if (studyTimeInput != null) {
            try {
                int studyTimeInSeconds = Integer.parseInt(studyTimeInput) * 3600; // Convert hours to seconds
                session.setAttribute("studyTimeLeft", studyTimeInSeconds);
                session.setAttribute("isBreak", false);
                session.setAttribute("status", "Focus Time");
            } catch (NumberFormatException e) {
                // Handle invalid input gracefully
                session.setAttribute("studyTimeLeft", 0);
                session.setAttribute("status", "Input tidak valid. Silakan coba lagi.");
            }
        }

        response.sendRedirect("pomodoro.jsp");
    }

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session = request.getSession();

    // Retrieve session attributes
    Integer studyTimeLeft = (Integer) session.getAttribute("studyTimeLeft");
    Boolean isBreak = (Boolean) session.getAttribute("isBreak");
    String status = (String) session.getAttribute("status");
    Integer currentIntervalTime = (Integer) session.getAttribute("currentIntervalTime");

    // Default values if attributes are null
    if (studyTimeLeft == null) studyTimeLeft = 0;
    if (isBreak == null) isBreak = false;
    if (status == null) status = "Tetapkan waktu belajar Anda untuk memulai!";
    if (currentIntervalTime == null) currentIntervalTime = 10; // Default focus time interval (10 seconds for testing)

    // Focus and break interval durations
    int focusInterval = 10; // Hardcoded as 10 seconds for testing
    int breakInterval = 5;  // Hardcoded as 5 seconds for testing

    // Decrement the global study timer and the current interval timer
    if (studyTimeLeft > 0 && currentIntervalTime > 0) {
        studyTimeLeft--;
        currentIntervalTime--;

        session.setAttribute("studyTimeLeft", studyTimeLeft);
        session.setAttribute("currentIntervalTime", currentIntervalTime);

        // When the current interval ends, toggle modes
        if (currentIntervalTime == 0) {
            isBreak = !isBreak;
            session.setAttribute("isBreak", isBreak);

            // Reset the current interval time based on the new mode
            currentIntervalTime = isBreak ? breakInterval : focusInterval;
            session.setAttribute("currentIntervalTime", currentIntervalTime);

            // Update the status
            status = isBreak ? "Break Time" : "Waktu Fokus";
            session.setAttribute("status", status);
        }
    }

    // Respond with JSON
    response.setContentType("application/json");
    StringBuilder jsonResponse = new StringBuilder();
    jsonResponse.append("{")
                .append("\"timeLeft\": \"").append(studyTimeLeft).append("\", ")
                .append("\"status\": \"").append(status).append("\"")
                .append("}");
    response.getWriter().write(jsonResponse.toString());
}

}