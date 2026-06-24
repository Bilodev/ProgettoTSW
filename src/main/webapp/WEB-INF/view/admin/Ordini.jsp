<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    java.util.List<model.OrdineRiepilogo> ordini =
        (java.util.List<model.OrdineRiepilogo>) request.getAttribute("ordini");
    java.util.List<model.OrdineRiepilogo> clienti =
        (java.util.List<model.OrdineRiepilogo>) request.getAttribute("clienti");

    String vista      = (String) request.getAttribute("vista");
    String dataInizio = (String) request.getAttribute("dataInizio");
    String dataFine   = (String) request.getAttribute("dataFine");
    String utenteId   = (String) request.getAttribute("utenteId");

    boolean vistaClienti = "clienti".equals(vista);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin — Ordini</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">


</head>
<body>

<div class="navbar">
        <span class="navbar-brand">Gestione Ordini</span>
        <button class="navbar-hamburger" id="hamburgerBtn" aria-label="Menu" aria-expanded="false">&#9776;</button>
        <div class="navbar-links" id="navbarLinks">
            <a href="<%= request.getContextPath() %>/home">Home</a>
            <a href="<%= request.getContextPath() %>/admin/catalogo">Gestione Catalogo</a>
            <a href="<%= request.getContextPath() %>/ordini">I miei Ordini</a>
            <a href="<%= request.getContextPath() %>/cart">Carrello</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </div>
</div>

<%-- Tab vista --%>
<div class="tabs">
    <form method="get" action="<%= request.getContextPath() %>/admin/ordini" style="display:inline;">
        <input type="hidden" name="dataInizio" value="<%= dataInizio %>">
        <input type="hidden" name="dataFine"   value="<%= dataFine %>">
        <input type="hidden" name="vista"      value="ordini">
        <button type="submit" <%= !vistaClienti ? "style='font-weight:bold;'" : "" %>>Tutti gli ordini</button>
    </form>
    <form method="get" action="<%= request.getContextPath() %>/admin/ordini" style="display:inline;">
        <input type="hidden" name="dataInizio" value="<%= dataInizio %>">
        <input type="hidden" name="dataFine"   value="<%= dataFine %>">
        <input type="hidden" name="vista"      value="clienti">
        <button type="submit" <%= vistaClienti ? "style='font-weight:bold;'" : "" %>>Riepilogo per cliente</button>
    </form>
</div>

<%-- Filtri --%>
<form method="get" action="<%= request.getContextPath() %>/admin/ordini">
    <input type="hidden" name="vista" value="<%= vista != null ? vista : "ordini" %>">
    <div class="filtri">
        <div> 
            <label>Data inizio</label>
            <input type="date" name="dataInizio" value="<%= dataInizio %>">
        </div>
        <div>
            <label>Data fine</label>
            <input type="date" name="dataFine" value="<%= dataFine %>">
        </div>
        <% if (!vistaClienti) { %>
        <div>
            <label>Email cliente (opzionale)</label>
            <input type="text" name="utenteId" value="<%= utenteId != null ? utenteId : "" %>" placeholder="es. a@b.com">
        </div>
        <% } %>
        <div>
            <button type="submit">Filtra</button>
        </div>
    </div>
</form>

<%-- Vista: riepilogo per cliente --%>
<% if (vistaClienti) { %>
    <% if (clienti == null || clienti.isEmpty()) { %>
        <p>Nessun cliente trovato per il periodo selezionato.</p>
    <% } else { %>
        <table>
            <thead>
                <tr>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Numero ordini</th>
                    <th>Totale speso</th>
                    <th>Dettaglio</th>
                </tr>
            </thead>
            <tbody>
                <% for (model.OrdineRiepilogo c : clienti) { %>
                <tr>
                    <td><%= c.getUsername() %></td>
                    <td><%= c.getEmail() %></td>
                    <td><%= c.getNumOrdini() %></td>
                    <td class="totale"><%= String.format("%.2f", c.getTotaleSpeso()) %> €</td>
                    <td>
                        <form method="get" action="<%= request.getContextPath() %>/admin/ordini" style="display:inline;">
                            <input type="hidden" name="vista"      value="ordini">
                            <input type="hidden" name="dataInizio" value="<%= dataInizio %>">
                            <input type="hidden" name="dataFine"   value="<%= dataFine %>">
                            <input type="hidden" name="utenteId"   value="<%= c.getEmail() %>">
                            <button type="submit">Vedi ordini</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>

<%-- Vista: lista ordini --%>
<% } else { %>
    <% if (ordini == null || ordini.isEmpty()) { %>
        <p>Nessun ordine trovato per il periodo selezionato.</p>
    <% } else { %>
        <table>
            <thead>
                <tr>
                    <th>Numero ordine</th>
                    <th>Data</th>
                    <th>Cliente</th>
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
                    <td><%= o.getUsername() %><br><small><%= o.getEmail() %></small></td>
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
<% } %>

</body>
<script src="${pageContext.request.contextPath}/scripts/hamburger.js"></script>
</html>