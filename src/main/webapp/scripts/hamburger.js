    const hamburgerBtn = document.getElementById('hamburgerBtn');
    const navbarLinks = document.getElementById('navbarLinks');
    hamburgerBtn.addEventListener('click', function () {
        const isOpen = navbarLinks.classList.toggle('open');
        hamburgerBtn.setAttribute('aria-expanded', isOpen);
    });
    // Chiudi il menu cliccando fuori
    document.addEventListener('click', function (e) {
        if (!hamburgerBtn.contains(e.target) && !navbarLinks.contains(e.target)) {
            navbarLinks.classList.remove('open');
            hamburgerBtn.setAttribute('aria-expanded', 'false');
        }
    });