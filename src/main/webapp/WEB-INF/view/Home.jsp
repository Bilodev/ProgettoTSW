<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="UTF-8">
        <title>Home</title>
    </head>

    <body>
        <% javax.servlet.http.HttpSession currentSession=(javax.servlet.http.HttpSession)
            request.getAttribute("session"); String nome=null; boolean isAdmin=false; if (currentSession !=null) {
            Object nomeObj=currentSession.getAttribute("username"); nome=nomeObj !=null ? nomeObj.toString() : null; Object
            adminObj=currentSession.getAttribute("admin"); isAdmin=Boolean.TRUE.equals(adminObj); } java.util.List<?>
            results = (java.util.List<?>) request.getAttribute("results");
    String query = request.getParameter("q");
%>
<div class="navbar">
    <% if (currentSession != null && nome != null && !nome.isEmpty()) { %>
        <span class="navbar-brand">Benvenuto <%= nome %></span>
        <button class="navbar-hamburger" id="hamburgerBtn" aria-label="Menu" aria-expanded="false">&#9776;</button>
        <div class="navbar-links" id="navbarLinks">
            <% if (isAdmin) { %>
                <a href="<%= request.getContextPath() %>/admin/catalogo">Gestione Catalogo</a>
                <a href="<%= request.getContextPath() %>/admin/ordini">Gestisci ordini</a>
            <% } %>
            <a href="<%= request.getContextPath() %>/ordini">I miei Ordini</a>
            <a href="<%= request.getContextPath() %>/cart">Carrello</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </div>
    <% } else { %>
        <span class="navbar-brand">Benvenuto</span>
        <button class="navbar-hamburger" id="hamburgerBtn" aria-label="Menu" aria-expanded="false">&#9776;</button>
        <div class="navbar-links" id="navbarLinks">
        	<a href="<%= request.getContextPath() %>/cart">Carrello</a>
            <a href="<%= request.getContextPath() %>/login">Login</a>
        </div>
    <% } %>
</div>
	<br> <br> <br>
    <input type="text" id="q" placeholder="Digita il nome del film...">
    <div id="message" style="margin-top: 10px;"></div>

<div id="resultsContainer" style="margin-top: 20px;"></div>
<script lang="js">
const searchInput = document.getElementById('q');
const resultsContainer = document.getElementById('resultsContainer');
const messageDiv = document.getElementById('message');
const contextPath = '<%= request.getContextPath() %>';	
</script>
<script src="${pageContext.request.contextPath}/scripts/home.js"></script>
<script src="${pageContext.request.contextPath}/scripts/hamburger.js"></script>

</body>
</html>