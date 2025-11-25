function alternarImagem() {
    const banner = document.getElementById('bannerContainer');
    const button = document.getElementById('toggleImageBtn');
    const icon = button.querySelector('.material-symbols-rounded');

    if (banner.classList.contains('is-collapsed')) {
        banner.classList.remove('is-collapsed');
        button.setAttribute('title', 'Recolher Banner');
        icon.textContent = 'expand_less';
    } else {
        banner.classList.add('is-collapsed');
        button.setAttribute('title', 'Expandir Banner');
        icon.textContent = 'expand_more';
    }
}
AOS.init({ duration: 1000, once: true });

particlesJS("particles-js", {
    particles: {
        number: { value: 60 },
        color: { value: "#d4a017" },
        size: { value: 3 },
        move: { enable: true, speed: 0.5 },
        line_linked: { enable: false }
    },
    interactivity: { events: { onhover: { enable: true, mode: "repulse" } } }
});

function alternarImagem() {
    const banner = document.querySelector('.profile-banner');
    banner.classList.toggle('collapsed');
}

// Contadores animados (se quiser manter o efeito)
document.querySelectorAll('.stat-number').forEach(el => {
    const target = parseInt(el.textContent || '0', 10);
    let count = 0;
    const inc = Math.max(1, Math.ceil(target / 50));
    const timer = setInterval(() => {
        count += inc;
        if (count >= target) {
            el.textContent = target;
            clearInterval(timer);
        } else {
            el.textContent = count;
        }
    }, 30);
});

