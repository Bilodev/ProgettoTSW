<%@page import="java.math.RoundingMode"%>
<%@page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Carrello</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.DVDInCart" %>
<%
    javax.servlet.http.HttpSession currentSession = request.getSession(false);
    ArrayList<DVDInCart> cartList = currentSession != null ? (ArrayList<DVDInCart>) currentSession.getAttribute("cart") : new ArrayList<DVDInCart>();
%>
<h1>Il Mio Carrello</h1>
<p><a href="<%= request.getContextPath() %>/home">Torna alla home</a></p>
<br> <br>
<% if (cartList == null || cartList.isEmpty()) { %>
    <p>Il carrello è vuoto.</p>
<% } else { %>
    <table>
        <thead>
            <tr>
                <th>Nome</th>
                <th>Durata</th>
                <th>Regista</th> 
                <th>Prezzo</th>                               
                <th>Quantità</th>
                <th>Azioni</th>
            </tr>
        </thead>
        <tbody>
        <% for (int i = 0; i < cartList.size(); i++) { %>
            <tr>
                <td><%= cartList.get(i).getNome() %></td>
                <td><%= cartList.get(i).getDurata() %> min</td>
                <td><%= cartList.get(i).getRegista() %></td>
                <td><%= cartList.get(i).getPrezzo() %>€</td>
				<td>
					<%= cartList.get(i).getQuantity() %> 
					<button class="qtbutton" onclick="updateCart(<%= cartList.get(i).getId() %>, 1 )">+</button> 
					<% if (cartList.get(i).getQuantity() >= 2){ %>
						<button class="qtbutton" onclick="updateCart(<%= cartList.get(i).getId() %>, -1 )">-</button> 
					<% } %>
				</td>
                
                <td class="actions">
                    <button onclick="removeFromCart(<%= cartList.get(i).getId() %>)">Rimuovi</button>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
<% } %>
	<br> <br>
	<% if (cartList != null && !cartList.isEmpty() && currentSession.getAttribute("nome") != null) { %>
		
	<% 
	  float sum = 0;
	  for (int i = 0; i < cartList.size(); i++)
	      sum += cartList.get(i).getPrezzo() * cartList.get(i).getQuantity();
	  DecimalFormat df = new DecimalFormat("#.##");
	  df.setRoundingMode(RoundingMode.CEILING);
	%>
	<form action="<%= request.getContextPath() %>/checkout" method="get" style="display: inline;">
	    <button type="submit">Checkout <%= df.format(sum) %> €</button>
	</form>
	<%  } else if (cartList != null && !cartList.isEmpty())  {	%>
		<form action="<%= request.getContextPath() %>/login" method="get" style="display: inline;">
            <button type="submit">LogIn per acquistare</button>
        </form>
	<% } %>
	<br>
	<% if (cartList != null && !cartList.isEmpty()) {%>
	<button onclick="svuotaCarrello()">Svuota Carrello</button>
	<% } %>
</body>
<script>
	const URL = "<%= request.getContextPath() %>/cart"
</script>
<script src="${pageContext.request.contextPath}/scripts/cart.js"></script>

</html>
