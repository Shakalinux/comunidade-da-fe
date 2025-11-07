

function showForm(id) {
    const form = document.getElementById('form-reflexao-' + id);
    const textoDiv = document.getElementById('reflexao-texto-' + id);
    const textarea = form.querySelector('textarea');
    const actions = document.querySelector(`#card-${id} .button-actions`);


    let originalText = '';
    if (textoDiv) {
        const pTag = textoDiv.querySelector('p');
        if (pTag) {
            originalText = pTag.textContent.trim();
        }
    }


    textarea.value = originalText;

    form.style.display = 'block';
    if (textoDiv) textoDiv.style.display = 'none';
    if (actions) actions.style.display = 'none';
}

function cancelForm(id) {
    const form = document.getElementById('form-reflexao-' + id);
    const texto = document.getElementById('reflexao-texto-' + id);
    const actions = document.querySelector(`#card-${id} .button-actions`);

    form.style.display = 'none';
    if (texto) texto.style.display = 'block';
    if (actions) actions.style.display = 'flex';
}

function salvarReflexao(event, id) {
    event.preventDefault();

    const form = document.getElementById('form-reflexao-' + id);
    const textarea = form.querySelector('textarea');
    const csrfInput = form.querySelector('input[name="_csrf"]');

    const csrfToken = csrfInput ? csrfInput.value : '';
    const actions = document.querySelector(`#card-${id} .button-actions`);


    const requestBody = new URLSearchParams({
        reflexao: textarea.value,
        _csrf: csrfToken
    });

    fetch(`/favorites/salvar-reflexao/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: requestBody
    })
        .then(response => {

            if (!response.ok) {

                return response.json().then(errorData => {
                    throw new Error(errorData.error || `Erro HTTP: ${response.status}. Verifique a autenticação ou o servidor.`);
                }).catch(() => {

                    throw new Error(`Erro HTTP: ${response.status}. Falha ao processar a resposta do servidor.`);
                });
            }
            return response.json();
        })
        .then(data => {

            if (!data.success) {
                throw new Error(data.error || 'Falha desconhecida ao salvar reflexão.');
            }


            let textoDiv = document.getElementById('reflexao-texto-' + id);

            if (!textoDiv) {
                textoDiv = document.createElement('div');
                textoDiv.id = 'reflexao-texto-' + id;
                textoDiv.className = 'reflexao-texto mt-2';
                const card = document.getElementById('card-' + id);
                card.insertBefore(textoDiv, form);
            }


            textoDiv.innerHTML = `<strong>Minha reflexão:</strong><p>${textarea.value}</p>`;

            form.style.display = 'none';
            textoDiv.style.display = 'block';
            if (actions) {
                actions.style.display = 'flex';
                const buttonReflexao = actions.querySelector('button.btn-primary, button.btn-warning');
                if (buttonReflexao) {
                    buttonReflexao.textContent = 'Editar Reflexão';
                    buttonReflexao.classList.remove('btn-primary');
                    buttonReflexao.classList.add('btn-warning');
                }
            }

            alert(data.message || "Reflexão salva com sucesso!");
        })
        .catch(err => alert(err.message));
}
