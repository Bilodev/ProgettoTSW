<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    javax.servlet.http.HttpSession currentSession = request.getSession(false);
    java.util.List<model.OrdineRiepilogo> ordini =
        (java.util.List<model.OrdineRiepilogo>) request.getAttribute("ordini");
    boolean isAdmin=false; 
    Object adminObj=currentSession.getAttribute("admin"); isAdmin=Boolean.TRUE.equals(adminObj); 
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>I miei ordini</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
<div class="navbar">
        <span class="navbar-brand">I miei ordini</span>
        <button class="navbar-hamburger" id="hamburgerBtn" aria-label="Menu" aria-expanded="false">&#9776;</button>
        <div class="navbar-links" id="navbarLinks">
            <a href="<%= request.getContextPath() %>/home">Home</a>
            <% if (isAdmin) { %>
                <a href="<%= request.getContextPath() %>/admin/catalogo">Gestione Catalogo</a>
                <a href="<%= request.getContextPath() %>/admin/ordini">Gestisci ordini</a>
            <% } %>
            <a href="<%= request.getContextPath() %>/cart">Carrello</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </div>
</div>
	<br> <br> <br>
<% if (ordini == null || ordini.isEmpty()) { %>
    <p>Non hai ancora effettuato nessun ordine.</p>
<% } else { %>
    <table>
        <thead>
            <tr>
                <th>Numero ordine</th>
                <th>Data</th>
                <th>DVD acquistati</th>
                <th>Indirizzo di consegna</th>
                <th>Totale</th>
            </tr>
        </thead>
        <tbody>
            <% for (model.OrdineRiepilogo o : ordini) { %>
            <tr>
                <td style="font-family: monospace; font-size: 11px;"><%= o.getSeqId() %></td>
                <td><%= o.getDataOrdine() %></td>
                <td><%= o.getDvdAcquistati() %></td>
                <td>
                    <%= o.getNome() %> <%= o.getCognome() %><br>
                    <%= o.getIndirizzo() %>, <%= o.getCitta() %> <%= o.getCap() %>, <%= o.getPaese() %>
                </td>
                <td class="totale"><%= String.format("%.2f", o.getTotale()) %> €</td>
            </tr>
            <% } %>
        </tbody>
    </table>
<% } %>

</body>
<script src="${pageContext.request.contextPath}/scripts/hamburger.js"></script>

</html>