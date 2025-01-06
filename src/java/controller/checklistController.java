package controller;

import com.google.gson.Gson;
import model.ListActivity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/checklist")
public class checklistController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("clearAndRedirect".equals(action)) {
            ListActivity.getAllActivities().clear();

            HttpSession session = request.getSession();
            session.removeAttribute("timeLeft");
            session.removeAttribute("timerRunning");
            session.removeAttribute("currentActivityIndex");
            
            session.setAttribute("activitiesStarted", false);

            response.sendRedirect("schedule");
            return;
        }

        HttpSession session = request.getSession();
        Integer timeLeft = (Integer) session.getAttribute("timeLeft");
        Boolean timerRunning = (Boolean) session.getAttribute("timerRunning");
        Integer currentActivityIndex = (Integer) session.getAttribute("currentActivityIndex");

        if (timeLeft == null) {
            timeLeft = 0;
            session.setAttribute("timeLeft", timeLeft);
        }
        if (timerRunning == null) {
            timerRunning = false;
            session.setAttribute("timerRunning", timerRunning);
        }
        if (currentActivityIndex == null) {
            currentActivityIndex = 0;
            session.setAttribute("currentActivityIndex", currentActivityIndex);
        }

        List<ListActivity> activities = ListActivity.getAllActivities();
        request.setAttribute("activities", activities);
        request.setAttribute("timeLeft", timeLeft);
        request.setAttribute("timerRunning", timerRunning);

        String requestedWith = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);

        if (isAjax) {
            response.setContentType("application/json");
            response.getWriter().write("{" +
                    "\"timeLeft\": " + timeLeft + ", " +
                    "\"timerRunning\": " + timerRunning +
                    "}");
        } else {
            request.getRequestDispatcher("/checklist.jsp").forward(request, response);
        }
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    HttpSession session = request.getSession();

    if ("addActivity".equals(action)) {
        Boolean activitiesStarted = (Boolean) session.getAttribute("activitiesStarted");
        if (activitiesStarted != null && activitiesStarted) {
            response.sendRedirect("checklist"); // Redirect if activities have started
            return;
        }
        String activityName = request.getParameter("activityName");
        int duration = Integer.parseInt(request.getParameter("duration"));
        ListActivity.addActivity(activityName, duration);
    }

    if ("startTimer".equals(action)) {
        List<ListActivity> allActivities = ListActivity.getAllActivities().stream()
            .filter(activity -> !activity.isCompleted())
            .collect(Collectors.toList());

        if (!allActivities.isEmpty()) {
            session.setAttribute("timeLeft", allActivities.get(0).getDuration() * 60);
            session.setAttribute("timerRunning", true);
            session.setAttribute("currentActivityIndex", 0);
            session.setAttribute("activitiesStarted", true); // Mark activities as started
            for (ListActivity activity : allActivities) {
                activity.setChecked(true);
            }
        }
    }

    if ("nextActivity".equals(action)) {
    List<ListActivity> checkedActivities = ListActivity.getCheckedActivities();
    Integer currentActivityIndex = (Integer) session.getAttribute("currentActivityIndex");

    if (currentActivityIndex == null) {
        currentActivityIndex = 0;
    }

    if (currentActivityIndex < checkedActivities.size()) {
        checkedActivities.get(currentActivityIndex).setCompleted(true);
    }

    if (currentActivityIndex + 1 < checkedActivities.size()) {
        session.setAttribute("currentActivityIndex", currentActivityIndex + 1);
        session.setAttribute("timeLeft", checkedActivities.get(currentActivityIndex + 1).getDuration() * 60);
    } else {
        session.setAttribute("timerRunning", false);
        session.setAttribute("timeLeft", 0);
    }

    // Send updated activities list as JSON
    response.setContentType("application/json");
    response.getWriter().write(new Gson().toJson(checkedActivities));
}


    response.sendRedirect("checklist");
}

}
