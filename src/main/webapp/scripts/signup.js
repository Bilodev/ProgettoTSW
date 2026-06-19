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