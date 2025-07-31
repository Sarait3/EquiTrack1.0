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

@WebServlet("/AddEquipment")
@MultipartConfig
public class AddEquipmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("Login");
            return;
        }

        if (user.getRole().equalsIgnoreCase("Regular")) {
            request.setAttribute("message", "Access Denied: You do not have permission to access this page.");
            request.setAttribute("success", false);
            request.getRequestDispatcher("/WEB-INF/Views/AccessDenied.jsp").forward(request, response);
            return;
        }

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

        request.setAttribute("formHtml", builder.createForm(false, true));
        request.setAttribute("user", user);
        request.setAttribute("sidebarStrategy",
            user.getRole().equalsIgnoreCase("Regular") ? new RegularUserPageStrategy() : new ManagerPageStrategy());

        request.getRequestDispatcher("/WEB-INF/Views/AddEquipment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EquipmentDao dao = new EquipmentDao();
        String imagePath = null;

        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String notes = request.getParameter("notes");
        boolean isOperational = Boolean.parseBoolean(request.getParameter("isOperational"));

        Part filePart = request.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            filePart.write(getServletContext().getRealPath("/") + "images/" + fileName);
            imagePath = "images/" + fileName;
        }

        Equipment equipment = new Equipment.Builder()
                .setName(name)
                .setOperational(isOperational)
                .setLocation(location)
                .setImagePath(imagePath)
                .setNotes(notes)
                .build();

        dao.createEquipment(equipment);

        request.setAttribute("message", "Equipment created successfully");
        request.setAttribute("isSuccessful", true);
        request.setAttribute("returnTo", "ListView");
        request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
    }
}
