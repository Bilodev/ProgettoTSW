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
                    let html = '<table><thead><tr><th>Nome</th><th>Durata</th><th>Regista</th><th>Prezzo</th><th>Azioni</th></tr></thead><tbody>';
                    data.forEach(dvd => {
                        html += '<tr><td>' + dvd.nome + '</td><td>' + dvd.durata + ' min</td><td>' + dvd.regista + '</td>' + '<td>'+ dvd.prezzo +'€</td>'; 
                        html += '<td><button onclick="addToCart(' + dvd.id + ', \'' + dvd.nome.replace(/'/g, "\\'") + '\', ' + dvd.durata + ', \'' + dvd.regista.replace(/'/g, "\\'") + '\', '+ dvd.prezzo + '\)">Aggiungi al carrello</button></td>';
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

    function addToCart(id, nome, durata, regista, prezzo) {
        fetch(contextPath + '/cart', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'action=add&id=' + id + '&nome=' + encodeURIComponent(nome) + '&durata=' + durata + '&regista=' + encodeURIComponent(regista) + '&prezzo=' + prezzo	
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
