<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Admin Catalogo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico?v=1">
</head>
<body>
<%
    String mode = (String) request.getAttribute("mode");
    java.util.List<?> dvds = (java.util.List<?>) request.getAttribute("dvds");
    model.DVD dvd = (model.DVD) request.getAttribute("dvd");
    javax.servlet.http.HttpSession currentSession = request.getSession(false);
    boolean isAdmin=false; 
    Object adminObj=currentSession.getAttribute("admin"); isAdmin=Boolean.TRUE.equals(adminObj);
%>

<div class="navbar">
        <span class="navbar-brand">Gestione Catalogo</span>
        <button class="navbar-hamburger" id="hamburgerBtn" aria-label="Menu" aria-expanded="false">&#9776;</button>
        <div class="navbar-links" id="navbarLinks">
              <a href="<%= request.getContextPath() %>/home">Home</a>
         
              <a href="<%= request.getContextPath() %>/admin/ordini">Gestisci ordini</a>
   
            <a href="<%= request.getContextPath() %>/ordini">I miei Ordini</a>
            <a href="<%= request.getContextPath() %>/cart">Carrello</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </div>
</div>

<% if ("list".equals(mode)) { %>
    <form action="<%= request.getContextPath() %>/admin/catalogo" method="get">
        <input type="hidden" name="action" value="add" />
        <button type="submit">Aggiungi nuovo DVD</button>
    </form>
	<br>
    <table>
        <thead>
            <tr>
                <th>Immagine</th>
                <th>Nome</th>
                <th>Durata</th>
                <th>Regista</th>
                <th>Prezzo</th>
                <th>Quantità</th>
                <th>Azioni</th>
            </tr>
        </thead>
        <tbody>
        <% if (dvds != null && !dvds.isEmpty()) {
            for (Object item : dvds) {
                model.DVD current = (model.DVD) item;
        %>
            <tr>
                <td>
                    <img src="<%= request.getContextPath() %>/static/<%= current.getId() %>.jpg"
                         alt="<%= current.getNome() %>"
                         width="60"
                         onerror="this.style.display='none'"/>
                </td>
                <td><%= current.getNome() %></td>
                <td><%= current.getDurata() %> min</td>
                <td><%= current.getRegista() %></td>
                <td><%= current.getPrezzo() %>€</td>
                <td><%= current.getQuantita() %></td>
                <td>
                    <form action="<%= request.getContextPath() %>/admin/catalogo" method="get" style="display:inline;">
                        <input type="hidden" name="action" value="edit" />
                        <input type="hidden" name="id" value="<%= current.getId() %>" />
                        <button type="submit">Modifica</button>
                    </form>
                    <% if (current.isInCatalogo()) { %>
                    <form action="<%= request.getContextPath() %>/admin/catalogo" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete" />
                        <input type="hidden" name="id" value="<%= current.getId() %>" />
                        <button type="submit">Elimina</button>
                    </form>
                    <% } else { %>
                    <form action="<%= request.getContextPath() %>/admin/catalogo" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="reinsert" />
                        <input type="hidden" name="id" value="<%= current.getId() %>" />
                        <button type="submit">Riaggiungi</button>
                    </form>
                    <% } %>
                </td>
            </tr>
        <%  }
        } else { %>
            <tr><td colspan="7">Nessun DVD presente.</td></tr>
        <% } %>
        </tbody>
    </table>

<% } else if ("add".equals(mode) || "edit".equals(mode)) { %>
    <h2><%= "add".equals(mode) ? "Aggiungi DVD" : "Modifica DVD" %></h2>

    <%-- enctype multipart/form-data obbligatorio per l'upload del file --%>
    <form action="<%= request.getContextPath() %>/admin/catalogo"
          method="post"
          enctype="multipart/form-data">

        <input type="hidden" name="action" value="<%= "add".equals(mode) ? "create" : "update" %>" />
        <% if ("edit".equals(mode) && dvd != null) { %>
            <input type="hidden" name="id" value="<%= dvd.getId() %>" />
        <% } %>

        <div>
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome"
                   value="<%= dvd != null ? dvd.getNome() : "" %>" required />
        </div>
        <div>
            <label for="durata">Durata (min):</label>
            <input type="number" id="durata" name="durata"
                   value="<%= dvd != null ? dvd.getDurata() : "" %>" required />
        </div>
        <div>
            <label for="regista">Regista:</label>
            <input type="text" id="regista" name="regista"
                   value="<%= dvd != null ? dvd.getRegista() : "" %>" required />
        </div>
        <div>
            <label for="prezzo">Prezzo (€):</label>
            <input type="number" id="prezzo" step="0.01" name="prezzo"
                   value="<%= dvd != null ? dvd.getPrezzo() : "" %>" required />
        </div>
        <div>
            <label for="quantita">Quantità:</label>
            <input type="number" id="quantita" name="quantita" min="0"
                   value="<%= dvd != null ? dvd.getQuantita() : "0" %>" required />
        </div>
        <div>
            <label for="immagine">Immagine:</label>
            <% if ("edit".equals(mode) && dvd != null) { %>
                <%-- Mostra l'immagine corrente se presente --%>
                <img src="<%= request.getContextPath() %>/static/img/dvd/<%= dvd.getId() %>.jpg"
                     alt="Immagine attuale"
                     width="80"
                     style="display:block; margin-bottom:6px;"
                     onerror="this.style.display='none'" />
                <small>Carica una nuova immagine solo se vuoi sostituire quella attuale.</small><br/>
            <% } %>
            <input type="file" id="immagine" name="immagine" accept="image/*" />
        </div>

        <button type="submit"><%= "add".equals(mode) ? "Crea" : "Aggiorna" %></button>
        <a href="<%= request.getContextPath() %>/admin/catalogo?action=list">Annulla</a>
    </form>

<% } else { %>
    <p>Modalità non valida.</p>
<% } %>
</body>
<script src="${pageContext.request.contextPath}/scripts/hamburger.js"></script>
</html>