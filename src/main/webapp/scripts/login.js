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