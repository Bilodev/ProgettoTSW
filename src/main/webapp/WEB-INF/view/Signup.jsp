<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Signup</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
<h1>Registrazione</h1>
<% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
<% } %>
<form action="<%= request.getContextPath() %>/signup" method="post" novalidate>
    <div>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" placeholder="esempio@dominio.com" value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" required>
    </div>
    <div>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" placeholder="3-20 caratteri: lettere, numeri, underscore" value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>" required pattern="^[A-Za-z0-9_]{3,20}$">
    </div>
    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" placeholder="minimo 6 caratteri" required pattern="^.{6,}$">
    </div>
    <div>
        <label for="confirm">Conferma Password:</label>
        <input type="password" id="confirm" name="confirm" placeholder="ripeti la password" required>
    </div>
    <center>
    <button type="submit">Registrati</button>
    </center>
</form>
<p>Hai già un account? <a href="<%= request.getContextPath() %>/login">Login</a></p>
<script src="${pageContext.request.contextPath}/scripts/signup.js"></script>
</body>
</html>
