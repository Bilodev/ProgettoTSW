<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body><br> <br>
<h1>Login</h1>
<% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
<% } %>
<form action="<%= request.getContextPath() %>/login" method="post" novalidate>
    <div>
        <label for="identifier">Username o Email:</label>
        <input type="text" id="identifier" name="identifier" placeholder="inserisci username o email" value="<%= request.getParameter("identifier") != null ? request.getParameter("identifier") : "" %>" required>
    </div>
    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" placeholder="minimo 6 caratteri" required>
    </div>
    <center>
    <button type="submit">Accedi</button>
    </center>
</form>
<p>Non hai un account? <a href="<%= request.getContextPath() %>/signup">Registrati</a></p>
<br>
<p><a href="<%= request.getContextPath() %>/home">Torna alla home</a></p>

<script src="${pageContext.request.contextPath}/scripts/login.js"></script>
</body>
</html>
