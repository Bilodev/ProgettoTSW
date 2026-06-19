// Funzione principale che effettua la chiamata AJAX all'API di ricerca
function eseguiRicerca(query) {
    messageDiv.textContent = '';

    // Costruiamo l'URL: se c'è testo cerchiamo per nome, se è vuoto caricherà tutto
    let url = contextPath + '/api/search?q=' + encodeURIComponent(query);

    fetch(url)
        .then(response => response.json())
        .then(data => {
            if (data.length === 0) {
                resultsContainer.innerHTML = '<p>Nessun DVD trovato.</p>';
            } else {
                let html = '<table><thead><tr><th>Copertina</th><th>Nome</th><th>Durata</th><th>Regista</th><th>Prezzo</th><th>Azioni</th></tr></thead><tbody>';
                data.forEach(dvd => {
                    html += '<tr>';
                    html += '<td><img src="static/' + dvd.id + '.jpg"/></td>';
                    html += '<td>' + dvd.nome + '</td>';
                    html += '<td>' + dvd.durata + ' min</td>';
                    html += '<td>' + dvd.regista + '</td>';
                    html += '<td>' + dvd.prezzo + '€</td>';
                    html += '<td><button onclick="addToCart(' + dvd.id + ', \'' + dvd.nome.replace(/'/g, "\\'") + '\', ' + dvd.durata + ', \'' + dvd.regista.replace(/'/g, "\\'") + '\', ' + dvd.prezzo + ')">Aggiungi al carrello</button></td>';
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
}

// Gestore dell'evento di digitazione sulla barra di ricerca
searchInput.addEventListener('input', function () {
    const query = this.value.trim();
    eseguiRicerca(query);
});

// Funzione di inserimento nel carrello (Invariata)
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

// Chiamata eseguita automaticamente al caricamento della pagina con query vuota
window.addEventListener('DOMContentLoaded', function() {
    eseguiRicerca(""); // Esegue la fetch iniziale caricando tutti i prodotti
});