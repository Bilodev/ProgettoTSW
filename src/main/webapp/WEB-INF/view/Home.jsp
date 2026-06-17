<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Home</title>
        <style>
            table {
                border-collapse: collapse;
                width: 100%;
            }

            th,
            td {
                border: 1px solid #ccc;
                padding: 8px;
            }

            .error {
                color: red;
            }
        </style>
    </head>

    <body>
        <% javax.servlet.http.HttpSession currentSession=(javax.servlet.http.HttpSession)
            request.getAttribute("session"); String nome=null; boolean isAdmin=false; if (currentSession !=null) {
            Object nomeObj=currentSession.getAttribute("nome"); nome=nomeObj !=null ? nomeObj.toString() : null; Object
            adminObj=currentSession.getAttribute("admin"); isAdmin=Boolean.TRUE.equals(adminObj); } java.util.List<?>
            results = (java.util.List
            <?>) request.getAttribute("results");
    String query = request.getParameter("q");
%>
<div class="navbar" style="margin-bottom: 20px; border-bottom: 2px solid #ccc; padding-bottom: 10px;">
<% if (currentSession != null && nome != null && !nome.isEmpty()) { %>
    <h1>Benvenuto <%= nome %></h1>
    <div class="buttons" style="display: flex; gap: 8px; align-items: center;">
        <% if (isAdmin) { %>
            <form action="<%= request.getContextPath() %>/admin" method="get" style="display: inline;">
                <button type="submit">Gestione Catalogo</button>
            </form>
        <% } %>
        <form action="<%= request.getContextPath() %>/logout" method="post" style="display: inline;">
            <button type="submit">Logout</button>
        </form>
    </div>
<% } else { %>
    <h1>Benvenuto</h1>
    <form action="<%= request.getContextPath() %>/login" method="get">
        <button type="submit">Login</button>
    </form>
<% } %>
        <form action="<%= request.getContextPath() %>/cart" method="get" style="display: inline;">
            <button type="submit">🛒 Carrello</button>
        </form>
</div>

<div>
    <label for="q">Cerca DVD:</label>
    <input type="text" id="q" placeholder="Digita il nome del film...">
    <div id="message" style="margin-top: 10px;"></div>
</div>

<div id="resultsContainer" style="margin-top: 20px;"></div>

<script>
    const searchInput = document.getElementById('q');
    const resultsContainer = document.getElementById('resultsContainer');
    const messageDiv = document.getElementById('message');
    const contextPath = '<%= request.getContextPath() %>';
    const isLoggedIn = <%= currentSession != null && nome != null && !nome.isEmpty() %>;

    searchInput.addEventListener('input', function () {
        const query = this.value.trim();
        messageDiv.textContent = '';

        if (query.length === 0) {
            resultsContainer.innerHTML = '';
            return;
        }

        fetch(contextPath + '/api/search?q=' + encodeURIComponent(query))
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    resultsContainer.innerHTML = '<p>Nessun DVD trovato.</p>';
                } else {
                    let html = '<h2>Risultati ricerca</h2><table><thead><tr><th>Nome</th><th>Durata</th><th>Regista</th><th>Azioni</th></tr></thead><tbody>';
                    data.forEach(dvd => {
                        html += '<tr><td>' + dvd.nome + '</td><td>' + dvd.durata + ' min</td><td>' + dvd.regista + '</td>';
                        html += '<td><button onclick="addToCart(' + dvd.id + ', \'' + dvd.nome.replace(/'/g, "\\'") + '\', ' + dvd.durata + ', \'' + dvd.regista.replace(/'/g, "\\'") + '\')">Aggiungi al carrello</button></td>';
                        html += '</tr>';
                    });
                    html += '</tbody></table>';
                    resultsContainer.innerHTML = html;
                }
            })
            .catch(error => {
                console.error('Errore:', error);
                resultsContainer.innerHTML = '<p style="color: red;">Errore nella ricerca.</p>';
            });
    });

    function addToCart(id, nome, durata, regista) {
        fetch(contextPath + '/cart', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'action=add&id=' + id + '&nome=' + encodeURIComponent(nome) + '&durata=' + durata + '&regista=' + encodeURIComponent(regista)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    messageDiv.textContent = '✓ "' + nome + '" aggiunto al carrello';
                    messageDiv.style.color = 'green';
                    setTimeout(() => {
                        messageDiv.textContent = '';
                    }, 3000);
                }
            })
            .catch(error => {
                console.error('Errore:', error);
                messageDiv.textContent = 'Errore nell\'aggiungere al carrello';
                messageDiv.style.color = 'red';
            });
    }
</script>
</body>
</html>