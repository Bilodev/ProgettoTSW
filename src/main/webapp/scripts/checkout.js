  document.getElementById('numero').addEventListener('input', function() {
    let v = this.value.replace(/\D/g, '').slice(0, 16);
    this.value = v.replace(/(.{4})/g, '$1 ').trim();
  });

  document.getElementById('scadenza').addEventListener('input', function() {
    let v = this.value.replace(/\D/g, '').slice(0, 4);
    if (v.length > 2) v = v.slice(0,2) + '/' + v.slice(2);
    this.value = v;
  });

  document.getElementById('cap').addEventListener('input', function() {
    this.value = this.value.replace(/\D/g, '').slice(0, 5);
  });

  document.getElementById('cvv').addEventListener('input', function() {
    this.value = this.value.replace(/\D/g, '').slice(0, 4);
  });