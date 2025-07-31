<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String message = (String) request.getAttribute("message");
    String returnTo = (String) request.getAttribute("returnTo");
    boolean isSuccessful = (Boolean) request.getAttribute("isSuccessful");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmation</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="header">
        <div class="header-content">
            <a href="<%= returnTo %>" class="back-btn">&larr; Back to List</a>
        </div>
    </div>

    <div class="container">
        <div class="empty-state">
            <h3 style="color:<%= isSuccessful ? "green" : "red" %>;">
                <%= message %>
            </h3>
        </div>
    </div>
</body>
</html>
