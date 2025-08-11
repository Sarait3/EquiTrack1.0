<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- Read data provided by the servlet --%>
<%
    String message = (String) request.getAttribute("message");     // text to display
    String returnTo = (String) request.getAttribute("returnTo");   // where the Back link goes
    boolean isSuccessful = (Boolean) request.getAttribute("isSuccessful"); // controls color
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmation</title>
    <link rel="stylesheet" href="css/style.css"> <%-- site styles --%>
</head>
<body>
    <div class="header">
        <div class="header-content">
            <%-- Back link --%>
            <a href="<%= returnTo %>" class="back-btn">&larr; Back to List</a>
        </div>
    </div>

    <div class="container">
        <div class="empty-state">
            <%-- Message color reflects success/failure. --%>
            <h3 style="color:<%= isSuccessful ? "green" : "red" %>;">
                <%= message %> <%-- message text from the servlet --%>
            </h3>
        </div>
    </div>
</body>
</html>
