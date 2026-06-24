// Funzione principale che effettua la chiamata AJAX all'API di ricerca
function eseguiRicerca(query) {
    messageDiv.textContent = '';
    let url = contextPath + '/api/search?q=' + encodeURIComponent(query);
    fetch(url)
        .then(response => response.json())
        .then(data => {
            if (data.length === 0) {
                resultsContainer.innerHTML = '<p>Nessun DVD trovato.</p>';
            } else {
let html = '<table>';
html += '<thead>';
html += '<tr>';
html += '<th>Copertina</th>';
html += '<th>Nome</th>';
html += '<th>Durata</th>';
html += '<th>Regista</th>';
html += '<th>Prezzo</th>';
html += '<th>Disponibilità</th>';
html += '<th>Azioni</th>';
html += '</tr>';
html += '</thead>';
html += '<tbody>';

data.forEach(dvd => {
    html += '<tr data-id="' + dvd.id + '">';
    
    // 2. Aggiungiamo l'attributo width protettivo all'immagine, come nel JSP
    html += '<td><img src="static/' + dvd.id + '.jpg"/></td>';
    
    html += '<td>' + dvd.nome + '</td>';
    html += '<td>' + dvd.durata + ' min</td>';
    html += '<td>' + dvd.regista + '</td>';
    html += '<td>' + dvd.prezzo + '€</td>';
    html += '<td class="qt-cell">' + (dvd.qt > 0 ? dvd.qt : '<span style="color:red;">Esaurito</span>') + '</td>';
    
    // 3. Ripristiniamo la classe corretta sul bottone (btn-addHome) per agganciare il CSS
    html += '<td><button class="btn-addHome" onclick="addToCart(' + dvd.id + ', \'' + dvd.nome.replace(/'/g, "\\'") + '\', ' + dvd.durata + ', \'' + dvd.regista.replace(/'/g, "\\'") + '\', ' + dvd.prezzo + ')"'
          + (dvd.qt <= 0 ? ' disabled' : '') + '>Aggiungi al carrello</button></td>';
          
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
    eseguiRicerca(this.value.trim());
});

// Funzione di inserimento nel carrello
function addToCart(id, nome, durata, regista, prezzo) {
    fetch(contextPath + '/cart', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'action=add&id=' + id + '&nome=' + encodeURIComponent(nome) + '&durata=' + durata + '&regista=' + encodeURIComponent(regista) + '&prezzo=' + prezzo
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            messageDiv.textContent = '\u2713 "' + nome + '" aggiunto al carrello';
            messageDiv.style.color = 'green';
            setTimeout(() => { messageDiv.textContent = ''; }, 3000);

            // Trova la riga della tabella tramite data-id e aggiorna la cella qt
            const row = resultsContainer.querySelector('tr[data-id="' + id + '"]');
            if (row) {
                const qtCell = row.querySelector('.qt-cell');
                const btn    = row.querySelector('.btn-add');
                let qt = parseInt(qtCell.textContent);
                if (!isNaN(qt)) {
                    qt -= 1;
                    if (qt <= 0) {
                        qtCell.innerHTML = '<span style="color:red;">Esaurito</span>';
                        btn.disabled = true;
                    } else {
                        qtCell.textContent = qt;
                    }
                }
            }
        } else {
            messageDiv.textContent = data.error || 'Errore nell\'aggiungere al carrello';
            messageDiv.style.color = 'red';
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
    eseguiRicerca("");
});