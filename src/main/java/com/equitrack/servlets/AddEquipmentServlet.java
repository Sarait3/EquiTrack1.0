package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.equitrack.dao.EquipmentDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.User;
import com.equitrack.service.FormBuilder;
import com.equitrack.service.ManagerPageStrategy;
import com.equitrack.service.RegularUserPageStrategy;

/**
 * Servlet that displays the Add Equipment form (GET)
 * and handles creation of new equipment (POST)
 *
 * Only non-Regular roles are allowed to access the form and create equipment
 */
@WebServlet("/AddEquipment")
@MultipartConfig
public class AddEquipmentServlet extends HttpServlet {

    /**
     * Shows the equipment creation form to authorized users.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @throws ServletException 
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Require a logged-in user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // Not authenticated: go to login
            response.sendRedirect("Login");
            return;
        }

        // Basic authorization: Regular users are blocked from this page
        if (user.getRole().equalsIgnoreCase("Regular")) {
            request.setAttribute("message", "Access Denied: You do not have permission to access this page.");

            request.setAttribute("success", false);
            request.getRequestDispatcher("/WEB-INF/Views/AccessDenied.jsp").forward(request, response);
            return;
        }

        // Build the form markup
        FormBuilder builder = new FormBuilder();
        builder.setAction("AddEquipment")
               .addRequiredInput("text", "Name:", "name")
               .addRequiredInput("text", "Location:", "location")
               .addFileInput("Image:", "imageFile", "image/*")
               .addSelect("Status:", "isOperational", new String[][] {
                   { "true", "Operational" },
                   { "false", "Out Of Service" }
               })
               .addInput("textarea", "Notes:", "notes");

        // Provide data to the JSP view
        request.setAttribute("formHtml", builder.createForm(false, true));
        request.setAttribute("user", user);
        // Choose sidebar strategy based on role
        request.setAttribute("sidebarStrategy",
            user.getRole().equalsIgnoreCase("Regular") ? new RegularUserPageStrategy() : new ManagerPageStrategy());

        // Render the Add Equipment page
        request.getRequestDispatcher("/WEB-INF/Views/AddEquipment.jsp").forward(request, response);
    }

    /**
     * Creates a new equipment record from submitted form data.
     *
     * @param request the HTTP servlet request containing multipart form data
     * @param response the HTTP servlet response
     * @throws ServletException 
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EquipmentDao dao = new EquipmentDao();
        String imagePath = null;

        // Read basic fields
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String notes = request.getParameter("notes");
        boolean isOperational = Boolean.parseBoolean(request.getParameter("isOperational"));

        // Handle optional image upload
        Part filePart = request.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            filePart.write(getServletContext().getRealPath("/") + "images/" + fileName);
            imagePath = "images/" + fileName; // store relative path for serving
        }

        // Build the domain object via the builder
        Equipment equipment = new Equipment.Builder()
                .setName(name)
                .setOperational(isOperational)
                .setLocation(location)
                .setImagePath(imagePath)
                .setNotes(notes)
                .build();

        // Persist the new equipment
        dao.createEquipment(equipment);

        // Prepare confirmation view
        request.setAttribute("message", "Equipment created successfully");
        request.setAttribute("isSuccessful", true); 
        request.setAttribute("returnTo", "ListView");
        request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
    }
}
