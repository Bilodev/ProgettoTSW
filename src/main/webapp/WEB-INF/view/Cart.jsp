<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Carrello</title>
<style>
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
    .actions { display: flex; gap: 8px; }
    button { padding: 5px 10px; cursor: pointer; }
</style>
</head>
<body>
<%
    javax.servlet.http.HttpSession currentSession = request.getSession(false);
    String cartJson = currentSession != null ? (String) currentSession.getAttribute("cart") : null;
    java.util.List<java.util.Map<String, String>> cartItems = new java.util.ArrayList<>();
    
    if (cartJson != null && !cartJson.isEmpty() && !cartJson.equals("[]")) {
        String content = cartJson.substring(1, cartJson.length() - 1);
        if (!content.isEmpty()) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\{[^}]+\\}");
            java.util.regex.Matcher m = p.matcher(cartJson);
            while (m.find()) {
                String item = m.group().replaceAll("[{}\"\\\\]", "");
                java.util.Map<String, String> map = new java.util.HashMap<>();
                String[] pairs = item.split(",");
                for (String pair : pairs) {
                    String[] kv = pair.split(":");
                    if (kv.length == 2) {
                        map.put(kv[0].trim(), kv[1].trim());
                    }
                }
                if (!map.isEmpty()) cartItems.add(map);
            }
        }
    }
%>
<h1>Il Mio Carrello</h1>
<p><a href="<%= request.getContextPath() %>/home">Torna alla home</a></p>

<% if (cartItems.isEmpty()) { %>
    <p>Il carrello è vuoto.</p>
<% } else { %>
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
        <% for (java.util.Map<String, String> item : cartItems) { %>
            <tr>
                <td><%= item.get("nome") %></td>
                <td><%= item.get("durata") %> min</td>
                <td><%= item.get("regista") %></td>
                <td class="actions">
                    <button onclick="removeFromCart(<%= item.get("id") %>)">Rimuovi</button>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
<% } %>
	<br> <br>
	<% if (!cartItems.isEmpty() && currentSession.getAttribute("nome") != null) { %>
		<form action="<%= request.getContextPath() %>/checkout" method="post" style="display: inline;">
            <button type="submit">Checkout</button>
        </form>
		
	<%  } else if (!cartItems.isEmpty())  {	%>
		<form action="<%= request.getContextPath() %>/login" method="get" style="display: inline;">
            <button type="submit">LogIn per acquistare</button>
        </form>
	<% } %>
<script>
function removeFromCart(id) {
    fetch('<%= request.getContextPath() %>/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'action=remove&id=' + id
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            location.reload();
        }
    })
    .catch(error => console.error('Errore:', error));
}
</script>
</body>
</html>
