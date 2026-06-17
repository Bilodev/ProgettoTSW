<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Signup</title>
<style>
    .error { color: red; }
    .invalid { border-color: red; }
</style>
</head>
<body>
<h1>Registrazione</h1>
<% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
<% } %>
<form action="<%= request.getContextPath() %>/signup" method="post" novalidate>
    <div>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" placeholder="esempio@dominio.com" value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>" required>
    </div>
    <div>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" placeholder="3-20 caratteri: lettere, numeri, underscore" value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>" required pattern="^[A-Za-z0-9_]{3,20}$">
    </div>
    <div>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" placeholder="minimo 6 caratteri" required pattern="^.{6,}$">
    </div>
    <div>
        <label for="confirm">Conferma Password:</label>
        <input type="password" id="confirm" name="confirm" placeholder="ripeti la password" required>
    </div>
    <button type="submit">Registrati</button>
</form>
<p>Hai già un account? <a href="<%= request.getContextPath() %>/login">Login</a></p>
<script>
const form = document.querySelector('form');
const emailInput = document.getElementById('email');
const usernameInput = document.getElementById('username');
const passwordInput = document.getElementById('password');
const confirmInput = document.getElementById('confirm');

const validateField = (field) => {
    field.classList.remove('invalid');
    if (!field.checkValidity()) {
        field.classList.add('invalid');
    }
};

const validatePasswordMatch = () => {
    if (passwordInput.value && confirmInput.value && passwordInput.value !== confirmInput.value) {
        passwordInput.classList.add('invalid');
        confirmInput.classList.add('invalid');
    } else if (passwordInput.value === confirmInput.value && passwordInput.checkValidity()) {
        passwordInput.classList.remove('invalid');
        confirmInput.classList.remove('invalid');
    }
};

emailInput.addEventListener('change', function() {
    validateField(this);
});

usernameInput.addEventListener('change', function() {
    validateField(this);
});

passwordInput.addEventListener('change', function() {
    validateField(this);
    validatePasswordMatch();
});

confirmInput.addEventListener('change', function() {
    validatePasswordMatch();
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
    if (passwordInput.value !== confirmInput.value) {
        passwordInput.classList.add('invalid');
        confirmInput.classList.add('invalid');
        valid = false;
    }
    if (!valid) {
        event.preventDefault();
    }
});
</script>
</body>
</html>
