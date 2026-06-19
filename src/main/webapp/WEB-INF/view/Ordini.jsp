<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    javax.servlet.http.HttpSession currentSession = request.getSession(false);
    java.util.List<model.OrdineRiepilogo> ordini =
        (java.util.List<model.OrdineRiepilogo>) request.getAttribute("ordini");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>I miei ordini</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>

<h1>I miei ordini</h1>
<div class="navbar">
    <div style="display: flex; gap: 8px; align-items: center;">
        <form action="<%= request.getContextPath() %>/home" method="get" style="display: inline;">
            <button type="submit">Torna al catalogo</button>
        </form>
    </div>
</div>

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
</html>