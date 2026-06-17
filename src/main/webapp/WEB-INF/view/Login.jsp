<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<style>
    .error { color: red; }
    .invalid { border-color: red; }
</style>
</head>
<body>
<h1>Login</h1>
<% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
<% } %>
<form action="<%= request.getContextPath() %>/login" method="post" novalidate>
    <div>
        <label for="identifier">Username o Email:</label>
        <input type="text" id="identifier" name="identifier" placeholder="inserisci username o email" value="<%= request.getParameter("identifier") != null ? request.getParameter("identifier") : "" %>" required>
    </div>
    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" placeholder="minimo 6 caratteri" required>
    </div>
    <button type="submit">Accedi</button>
</form>
<p>Non hai un account? <a href="<%= request.getContextPath() %>/signup">Registrati</a></p>
<script>
const form = document.querySelector('form');
const identifierInput = document.getElementById('identifier');
const passwordInput = document.getElementById('password');

const validateField = (field) => {
    field.classList.remove('invalid');
    if (!field.checkValidity()) {
        field.classList.add('invalid');
    }
};

identifierInput.addEventListener('change', function() {
    validateField(this);
});

passwordInput.addEventListener('change', function() {
    validateField(this);
});

form.addEventListener('submit', function (event) {
    let valid = true;
    const fields = Array.from(form.querySelectorAll('[required]'));
    fields.forEach(field => {
        field.classList.remove('invalid');
        if (!field.checkValidity()) {
            field.classList.add('invalid');
            valid = false;
        }
    });
    if (!valid) {
        event.preventDefault();
    }
});
</script>
</body>
</html>
