<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Pagamento</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/checkout.css">
</head>
<body>

<form class="card" method="post" action="<%= request.getContextPath() %>/checkout">
  <h1 class="card-title">Completa il pagamento</h1>

  <div class="form-section">
    <p class="section-title">Indirizzo di fatturazione</p>
    <div class="field-group">
      <div class="field-row">
        <div class="field">
          <label for="nome">Nome</label>
          <input type="text" id="nome" name="nome" placeholder="Mario" autocomplete="given-name" required>
        </div>
        <div class="field">
          <label for="cognome">Cognome</label>
          <input type="text" id="cognome" name="cognome" placeholder="Rossi" autocomplete="family-name" required>
        </div>
      </div>
      <div class="field">
        <label for="indirizzo">Indirizzo</label>
        <input type="text" id="indirizzo" name="indirizzo" placeholder="Via Roma 12" autocomplete="street-address" required>
      </div>
      <div class="field-row">
        <div class="field">
          <label for="citta">Città</label>
          <input type="text" id="citta" name="citta" placeholder="Milano" autocomplete="address-level2" required>
        </div>
        <div class="field">
          <label for="cap">CAP</label>
          <input type="text" id="cap" name="cap" placeholder="20121" maxlength="5" autocomplete="postal-code" inputmode="numeric" required>
        </div>
      </div>
      <div class="field">
        <label for="paese">Paese</label>
        <select id="paese" name="paese" autocomplete="country">
          <option value="IT" selected>Italia</option>
          <option value="DE">Germania</option>
          <option value="FR">Francia</option>
          <option value="ES">Spagna</option>
          <option value="GB">Regno Unito</option>
          <option value="US">Stati Uniti</option>
        </select>
      </div>
    </div>
  </div>

  <hr>

  <div class="form-section">
    <p class="section-title">Dati della carta</p>
    <div class="card-badges">
      <span class="badge">VISA</span>
      <span class="badge">Mastercard</span>
      <span class="badge">Amex</span>
    </div>
    <div class="field-group">
      <div class="field">
        <label for="titolare">Titolare della carta</label>
        <input type="text" id="titolare" name="titolare" placeholder="Mario Rossi" autocomplete="cc-name" required>
      </div>
      <div class="field">
        <label for="numero">Numero carta</label>
        <div class="input-icon-wrap">
          <input type="text" id="numero" name="numero" placeholder="1234 5678 9012 3456" maxlength="19" autocomplete="cc-number" inputmode="numeric" required>
        </div>
      </div>
      <div class="field-row">
        <div class="field">
          <label for="scadenza">Scadenza</label>
          <input type="text" id="scadenza" name="scadenza" placeholder="MM/AA" maxlength="5" autocomplete="cc-exp" inputmode="numeric" required>
        </div>
        <div class="field">
          <label for="cvv">CVV</label>
          <div class="input-icon-wrap">
            <input type="password" id="cvv" name="cvv" placeholder="123" maxlength="4" autocomplete="cc-csc" inputmode="numeric" required>
          </div>
        </div>
      </div>
    </div>
  </div>

  <button type="submit" class="pay-btn"> Paga ora</button>
  <p class="secure-note"> Pagamento sicuro e crittografato</p>

</form>

<script src="${pageContext.request.contextPath}/scripts/checkout.js"></script>

</body>
</html>