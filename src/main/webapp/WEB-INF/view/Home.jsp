<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    
        <meta charset="UTF-8">
        <title>Home</title>
    </head>

    <body>
        <% javax.servlet.http.HttpSession currentSession=(javax.servlet.http.HttpSession)
            request.getAttribute("session"); String nome=null; boolean isAdmin=false; if (currentSession !=null) {
            Object nomeObj=currentSession.getAttribute("nome"); nome=nomeObj !=null ? nomeObj.toString() : null; Object
            adminObj=currentSession.getAttribute("admin"); isAdmin=Boolean.TRUE.equals(adminObj); } java.util.List<?>
            results = (java.util.List
            <?>) request.getAttribute("results");
    String query = request.getParameter("q");
%>
<% if (currentSession != null && nome != null && !nome.isEmpty()) { %>
<h1>Benvenuto <%= nome %></h1>
<div class="navbar" style="margin-bottom: 20px; border-bottom: 2px solid #ccc; padding-bottom: 10px;">
    <div class="buttons" style="display: flex; gap: 8px; align-items: center;">
        <% if (isAdmin) { %>
            <form action="<%= request.getContextPath() %>/admin/catalogo" method="get" style="display: inline;">
                <button type="submit">Gestione Catalogo</button>
            </form>
			<form action="<%= request.getContextPath() %>/admin/ordini" method="get" style="display: inline;">
                <button type="submit">Gestisci ordini utenti</button>
            </form>
            
        <% } %>
        <form action="<%= request.getContextPath() %>/logout" method="post" style="display: inline;">
            <button type="submit">Logout</button>
        </form>
        
        <form action="<%= request.getContextPath() %>/ordini" method="get" style="display: inline;">
            <button type="submit">I miei Ordini</button>
        </form>
    </div>
<% } else { %>
    <h1>Benvenuto</h1>
    <form action="<%= request.getContextPath() %>/login" method="get">
        <button type="submit">Login</button>
    </form>
<% } %>
	<br>
</div>
        <form action="<%= request.getContextPath() %>/cart" method="get" style="display: inline;">
            <button type="submit">🛒 Carrello</button>
        </form>
	<br> <br>
<div>
    <label for="q">Cerca DVD:</label>
    <input type="text" id="q" placeholder="Digita il nome del film...">
    <div id="message" style="margin-top: 10px;"></div>
</div>

<div id="resultsContainer" style="margin-top: 20px;"></div>
<script lang="js">
const searchInput = document.getElementById('q');
const resultsContainer = document.getElementById('resultsContainer');
const messageDiv = document.getElementById('message');
const contextPath = '<%= request.getContextPath() %>';	
</script>
<script src="${pageContext.request.contextPath}/scripts/home.js">

</script>
</body>
</html>