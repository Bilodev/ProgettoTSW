<%@page import="java.math.RoundingMode"%>
<%@page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Carrello</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.DVDInCart" %>
<%
    javax.servlet.http.HttpSession currentSession = request.getSession(false);
    ArrayList<DVDInCart> cartList = currentSession != null ? (ArrayList<DVDInCart>) currentSession.getAttribute("cart") : new ArrayList<DVDInCart>();
    String nome=null;
    if (currentSession !=null) { Object nomeObj=currentSession.getAttribute("username"); nome=nomeObj !=null ? nomeObj.toString() : null; }
    	
    
    boolean isAdmin=false; 
    Object adminObj=currentSession.getAttribute("admin"); isAdmin=Boolean.TRUE.equals(adminObj);
%>

<div class="navbar">
    <% if (currentSession != null && nome != null && !nome.isEmpty()) { %>
        <span class="navbar-brand">Carrello</span>
        <button class="navbar-hamburger" id="hamburgerBtn" aria-label="Menu" aria-expanded="false">&#9776;</button>
        <div class="navbar-links" id="navbarLinks">
            <a href="<%= request.getContextPath() %>/home">Home</a>
            <% if (isAdmin) { %>
                <a href="<%= request.getContextPath() %>/admin/catalogo">Gestione Catalogo</a>
                <a href="<%= request.getContextPath() %>/admin/ordini">Gestisci ordini</a>
            <% } %>
            <a href="<%= request.getContextPath() %>/ordini">I miei Ordini</a>
            <a href="<%= request.getContextPath() %>/logout">Logout</a>
        </div>
    <% } else { %>
        <span class="navbar-brand">Benvenuto</span>
        <button class="navbar-hamburger" id="hamburgerBtn" aria-label="Menu" aria-expanded="false">&#9776;</button>
        <div class="navbar-links" id="navbarLinks">
        	<a href="<%= request.getContextPath() %>/cart">Home</a>
            <a href="<%= request.getContextPath() %>/login">Login</a>
        </div>
    <% } %>
</div>
<br> <br>
<% if (cartList == null || cartList.isEmpty()) { %>
    <p>Il carrello è vuoto.</p>
<% } else { %>
    <table>
        <thead>
            <tr>
            	<th>Copertina</th>
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
            	<td><img src="<%= request.getContextPath() %>/static/<%= cartList.get(i).getId()%>.jpg"></td>
                <td><%= cartList.get(i).getNome() %></td>
                <td><%= cartList.get(i).getDurata() %> min</td>
                <td><%= cartList.get(i).getRegista() %></td>
                <td><%= cartList.get(i).getPrezzo() %>€</td>
				<td>
					<%=cartList.get(i).getQuantitaSelezionata()%> 
					<button class="qtbutton" onclick="updateCart(<%=cartList.get(i).getId()%>, 1 )">+</button> 
					<%
 					if (cartList.get(i).getQuantitaSelezionata() >= 2){
 					%>
						<button class="qtbutton" onclick="updateCart(<%= cartList.get(i).getId() %>, -1 )">-</button> 
					<% } %>
				</td>
                
                <td>
                    <button onclick="removeFromCart(<%= cartList.get(i).getId() %>)">Rimuovi</button>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
<% } %>
	<br> <br>
	<% if (cartList != null && !cartList.isEmpty() && currentSession.getAttribute("username") != null) { %>
		
	<% 
	  float sum = 0;
	  for (int i = 0; i < cartList.size(); i++)
	      sum += cartList.get(i).getPrezzo() * cartList.get(i).getQuantitaSelezionata();
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
<script src="${pageContext.request.contextPath}/scripts/hamburger.js"></script>

</html>
