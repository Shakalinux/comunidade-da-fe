function alternarImagem() {
    var container = document.querySelector('.profile-imagem-primary');
    var btn = document.getElementById('toggleImageBtn');

    if (container.classList.contains('hidden')) {
        container.classList.remove('hidden');
        btn.textContent = 'Recolher Imagem';
    } else {
        container.classList.add('hidden');
        btn.textContent = 'Expandir Imagem';
    }
}
