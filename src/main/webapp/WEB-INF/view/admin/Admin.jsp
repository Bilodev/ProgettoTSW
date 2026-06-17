<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Catalogo</title>
<style>
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ccc; padding: 8px; }
    .actions { display: flex; gap: 8px; }
</style>
</head>
<body>
<%
    String mode = (String) request.getAttribute("mode");
    java.util.List<?> dvds = (java.util.List<?>) request.getAttribute("dvds");
    model.DVD dvd = (model.DVD) request.getAttribute("dvd");
%>
<h1>Gestione Catalogo</h1>
<p><a href="<%= request.getContextPath() %>/home">Torna alla home</a></p>

<% if ("list".equals(mode)) { %>
    <form action="<%= request.getContextPath() %>/admin" method="get">
        <input type="hidden" name="action" value="add" />
        <button type="submit">Aggiungi nuovo DVD</button>
    </form>

    <h2>Elenco DVD</h2>
    <table>
        <thead>
            <tr>
                <th>Nome</th>
                <th>Durata</th>
                <th>Regista</th>
                <th>Azioni</th>
            </tr>
        </thead>
        <tbody>
        <% if (dvds != null && !dvds.isEmpty()) {
            for (Object item : dvds) {
                model.DVD current = (model.DVD) item;
        %>
            <tr>
                <td><%= current.getNome() %></td>
                <td><%= current.getDurata() %> min</td>
                <td><%= current.getRegista() %></td>
                <td class="actions">
                    <form action="<%= request.getContextPath() %>/admin" method="get" style="display:inline;">
                        <input type="hidden" name="action" value="edit" />
                        <input type="hidden" name="id" value="<%= current.getId() %>" />
                        <button type="submit">Modifica</button>
                    </form>
                   <% if (current.isInCatalogo()) { %> 
                    <form action="<%= request.getContextPath() %>/admin" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete" />
                        <input type="hidden" name="id" value="<%= current.getId() %>" />
                        <button type="submit">Elimina</button>
                    </form>
					<% } else { %> 
					<form action="<%= request.getContextPath() %>/admin" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="reinsert" />
                        <input type="hidden" name="id" value="<%= current.getId() %>" />
                        <button type="submit">Riaggungi</button>
                    </form>
	               <% } %> 
                </td>
            </tr>
        <%  }
        } else { %>
            <tr><td colspan="4">Nessun DVD presente.</td></tr>
        <% } %>
        </tbody>
    </table>

<% } else if ("add".equals(mode) || "edit".equals(mode)) { %>
    <h2><%= "add".equals(mode) ? "Aggiungi DVD" : "Modifica DVD" %></h2>
    <form action="<%= request.getContextPath() %>/admin" method="post">
        <input type="hidden" name="action" value="<%= "add".equals(mode) ? "create" : "update" %>" />
        <% if ("edit".equals(mode) && dvd != null) { %>
            <input type="hidden" name="id" value="<%= dvd.getId() %>" />
        <% } %>
        <div>
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" value="<%= dvd != null ? dvd.getNome() : "" %>" required />
        </div>
        <div>
            <label for="durata">Durata (min):</label>
            <input type="number" id="durata" name="durata" value="<%= dvd != null ? dvd.getDurata() : "" %>" required />
        </div>
        <div>
            <label for="regista">Regista:</label>
            <input type="text" id="regista" name="regista" value="<%= dvd != null ? dvd.getRegista() : "" %>" required />
        </div>
        <button type="submit"><%= "add".equals(mode) ? "Crea" : "Aggiorna" %></button>
        <a href="<%= request.getContextPath() %>/admin?action=list">Annulla</a>
    </form>
<% } else { %>
    <p>Modalità non valida.</p>
<% } %>
</body>
</html>
