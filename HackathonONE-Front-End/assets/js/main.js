import { criarComentario, atualizarComentario, uploadCsv } from "./sentiment.js";
import { carregarStats } from "./stats.js";
import { listarComentarios, buscarComentario, deletarComentario } from "./crud.js";
import  {iniciarTypewriter}  from "./typewriter.js";


// Abrir caixa de comentário
document.addEventListener("DOMContentLoaded", () => {

iniciarTypewriter();
    
document.getElementById("btn-comentario").onclick = () => {
    document.getElementById("box-comentario").style.display = "block";
};


document.getElementById("btn-enviar-comentario").onclick = async () => {
    const textoInput = document.getElementById("texto-comentario");
    const resultadoDiv = document.getElementById("resultado-comentario");
    const texto = textoInput.value;

    try {
        const data = await criarComentario(texto);

        // VERIFICAÇÃO: Se data for um Array, significa que houve erro de validação (DTO)
        if (Array.isArray(data)) {
            // Pegamos a mensagem do primeiro erro da lista
            const mensagemErro = data[0].mensagem; 
            
            resultadoDiv.innerHTML = `
                <div class="result-box" style="border-color: var(--danger-color); color: var(--danger-color); margin-top: 10px;">
                    ⚠️ <strong>Erro de Validação:</strong> ${mensagemErro}
                </div>
            `;
            return; // Para a execução aqui
        }

        // Se não for array, segue o fluxo normal de sucesso
        resultadoDiv.innerHTML = `
            <div class="resultado-busca-container">
                <div class="resultado-linha">
                    <span class="resultado-label">Previsão</span>
                    <span class="resultado-badge ${data.previsao.toLowerCase()}">
                        ${data.previsao}
                    </span>
                </div>

                <div class="resultado-linha">
                    <span class="resultado-label">Probabilidade</span>
                    <strong>${(data.probabilidade * 100).toFixed(2)}%</strong>
                </div>

                <div class="resultado-linha">
                    <span class="resultado-label">Palavras relevantes</span>
                    <div class="analise-peso">
                        ${data.analise_peso
                            .map(p => `<span class="analise-peso-item">${p}</span>`)
                            .join("")}
                    </div>
                </div>
            </div>
        `;

        textoInput.value = ""; 
        carregarPagina();

    } catch (error) {
        console.error("Erro técnico:", error);
        resultadoDiv.innerHTML = `<p style="color: var(--danger-color);">Erro ao conectar com a API.</p>`;
    }
};

// Abrir seletor de arquivo
document.getElementById("btn-arquivo").addEventListener("click", () => {
    document.getElementById("arquivo-csv").click();
});

// Upload CSV
document.getElementById("arquivo-csv").addEventListener("change", async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    try {
        const data = await uploadCsv(file);
        const resultadoLote = document.getElementById("resultado-lote");
        
        // 1. Mostra e limpa classes de animação anteriores
        resultadoLote.style.display = "block";
        resultadoLote.style.opacity = "1";

        resultadoLote.innerHTML = `
            <div class="stats-display" style="border-style: solid; border-color: var(--primary-color); text-align: left;">
                <h4 style="color: var(--primary-color); margin-bottom: 10px;">Resumo do Upload</h4>
                <p>📊 <strong>Total Lidos:</strong> ${data.totalLidos}</p>
                <p>✅ <strong>Sucesso:</strong> <span style="color: var(--success-color)">${data.sucesso}</span></p>
                <p>❌ <strong>Erros:</strong> <span style="color: var(--danger-color)">${data.erro}</span></p>
                <hr style="margin: 10px 0; border-top: 1px dashed #323238;">
                <p style="font-size: 0.85rem; color: var(--text-secondary);">${data.mensagem}</p>
            </div>
        `;
        
        e.target.value = ""; 
        carregarPagina();

        // 2. Agenda para sumir após 5 segundos (5000ms)
        setTimeout(() => {
            // Adiciona um efeito de fade out via CSS transition
            resultadoLote.style.transition = "opacity 0.5s ease";
            resultadoLote.style.opacity = "0";
            
            // Depois que a transição de 0.5s acaba, esconde o elemento de vez
            setTimeout(() => {
                resultadoLote.style.display = "none";
                resultadoLote.innerHTML = ""; // Limpa o conteúdo
            }, 500); 
        }, 5000);

    } catch (error) {
        console.error("Erro no processamento:", error);
        alert("Erro ao processar arquivo.");
    }
});

// Estatísticas rápidas (10, 50, 100)
document.querySelectorAll(".btn-stats").forEach(btn => {
    btn.addEventListener("click", async () => {
        const qtd = btn.dataset.qtd;
        console.log("Buscando stats:", qtd);

        const data = await carregarStats(qtd);

        document.getElementById("resultado-stats").innerHTML = `
            <p>Positivo: ${data.positivo}%</p>
            <p>Negativo: ${data.negativo}%</p>
        `;
    });
});

// Estatística personalizada
document.getElementById("btn-stats-custom").addEventListener("click", async () => {
    const qtd = document.getElementById("stats-custom").value;

    if (!qtd || qtd <= 0) {
        alert("Digite uma quantidade válida");
        return;
    }

    console.log("Buscando stats custom:", qtd);

    const data = await carregarStats(qtd);

    document.getElementById("resultado-stats").innerHTML = `
        <p>Positivo: ${data.positivo}%</p>
        <p>Negativo: ${data.negativo}%</p>
    `;
});


// Buscar comentário
document.getElementById("btn-buscar").onclick = async () => {
    const id = document.getElementById("buscar-id").value;

    if (!id) {
        alert("Informe um ID válido");
        return;
    }

    const data = await buscarComentario(id);

    // VERIFICAÇÃO DE ERRO: Se o objeto 'data' contiver a chave 'error'
    if (data.error) {
        document.getElementById("resultado-busca").innerHTML = `
            <div class="result-box" style="border-color: var(--danger-color); color: var(--danger-color);">
                ⚠️ ${data.error} (ID: ${id})
            </div>
        `;
        return; // Para a execução aqui e não tenta rodar o código abaixo
    }

    // Se chegou aqui, o dado existe
    const previsaoTexto = data.previsao || "desconhecido";

    document.getElementById("resultado-busca").innerHTML = `
    <div class="resultado-busca-container">
        <div class="resultado-linha">
            <span class="resultado-label">Comentário</span>
            <p class="resultado-texto">${data.text}</p>
        </div>

        <div class="resultado-linha">
            <span class="resultado-label">Previsão</span>
            <span class="resultado-badge ${previsaoTexto.toLowerCase()}">
                ${previsaoTexto}
            </span>
        </div>

        <div class="resultado-linha">
            <span class="resultado-label">Probabilidade</span>
            <strong>${(data.probabilidade * 100).toFixed(2)}%</strong>
        </div>
    </div>`;
};


// Deletar comentário
document.getElementById("btn-deletar").onclick = async () => {
    const idInput = document.getElementById("deletar-id");
    const msgContainer = document.getElementById("mensagem-delete");
    const id = idInput.value;

    // 1. Limpa qualquer mensagem anterior antes de começar
    msgContainer.innerHTML = "";

    // 2. Validação de ID positivo
    if (!id || parseInt(id) <= 0) {
        msgContainer.style.color = "var(--danger-color)";
        msgContainer.innerHTML = "⚠️ Informe um ID positivo.";
        
        // Remove a mensagem de erro sozinha após 3 segundos
        setTimeout(() => { msgContainer.innerHTML = ""; }, 3000);
        return; 
    }

    // 3. Se passou na validação, prossegue com o confirm e o delete
    if (!confirm(`Deseja realmente excluir o ID ${id}?`)) return;

    try {
        const data = await deletarComentario(id);

        if (data && data.message === "Previsão excluída com sucesso") {
            msgContainer.style.color = "var(--success-color)";
            msgContainer.innerHTML = `✅ ${data.message}`;
            idInput.value = ""; // Limpa o campo de texto
            carregarPagina(); // Atualiza a lista
        } else {
            msgContainer.style.color = "var(--danger-color)";
            msgContainer.innerHTML = `❌ Erro: ${data?.message || "ID não encontrado"}`;
        }
        
        // Limpa a mensagem de sucesso ou erro do Java após 3 segundos
        setTimeout(() => { msgContainer.innerHTML = ""; }, 3000);

    } catch (error) {
        msgContainer.innerHTML = "❌ Erro ao conectar com o servidor.";
    }
};


   document.getElementById("btn-atualizar").onclick = async () => {
    const id = document.getElementById("atualizar-id").value;
    const text = document.getElementById("atualizar-texto").value;
    const resDiv = document.getElementById("resultado-atualizacao");

    if (!id || id <= 0) {
        resDiv.innerHTML = `<p style="color: var(--danger-color);">⚠️ Digite um ID positivo.</p>`;
        return;
    }

    try {
        const data = await atualizarComentario(id, text);

        // 1. Criamos os badges para as palavras relevantes
        const pesosHtml = data.analise_peso 
            ? data.analise_peso.map(p => `<span class="analise-peso-item">${p}</span>`).join('')
            : 'Nenhuma análise disponível';

        // 2. Montamos o HTML de resultado rico em detalhes
        resDiv.innerHTML = `
            <div class="resultado-busca-container" style="border-left: 4px solid var(--warning-color); padding-left: 15px;">
                <p style="color: var(--success-color); font-weight: bold; margin-bottom: 10px;">✅ Registro ${id} Atualizado!</p>
                
                <div class="resultado-linha">
                    <span class="resultado-label">Nova Previsão</span>
                    <span class="resultado-badge ${data.previsao.toLowerCase()}">
                        ${data.previsao}
                    </span>
                </div>

                <div class="resultado-linha">
                    <span class="resultado-label">Confiança</span>
                    <strong>${(data.probabilidade * 100).toFixed(2)}%</strong>
                </div>

                <div class="resultado-linha">
                    <span class="resultado-label">Palavras Relevantes</span>
                    <div class="analise-peso">
                        ${pesosHtml}
                    </div>
                </div>
            </div>
        `;

        // Limpa os campos após o sucesso
        document.getElementById("atualizar-id").value = "";
        document.getElementById("atualizar-texto").value = "";
        
        carregarPagina(); // Atualiza a lista lateral para mostrar o novo texto

    } catch (error) {
        resDiv.innerHTML = `
            <div class="result-box" style="border-color: var(--danger-color); color: var(--danger-color);">
                ❌ Erro: ${error.message}
            </div>
        `;
    }
};

let paginaAtual = 0;
const tamanhoPagina = 5;
async function carregarPagina() {
    try {
        const data = await listarComentarios(paginaAtual, tamanhoPagina);
        const lista = data.content;
        const container = document.getElementById("lista-comentarios");
        
        if (!container) return;

        container.innerHTML = ""; 

        lista.forEach(item => {
            const textoPrevisao = item.previsao.previsao; // Ex: "Positivo"
            const classeCor = textoPrevisao.toLowerCase(); // Ex: "positivo"

            container.innerHTML += `
                <div class="comentario-card">
                    <p class="resultado-texto">${item.text}</p>
                    <div class="comentario-footer">
                        <span class="resultado-badge ${classeCor}">
                            ${textoPrevisao}
                        </span>
                        <small style="color: var(--text-secondary); font-size: 0.75rem;">
                            ID: ${item.id}
                        </small>
                    </div>
                </div>
            `;
        });

        // Atualização da paginação
        const pageInfo = document.getElementById("page-info");
        if (pageInfo) {
            pageInfo.innerText = `Página ${data.number + 1} de ${data.totalPages}`;
        }

        const btnPrev = document.getElementById("prev-page");
        const btnNext = document.getElementById("next-page");
        
        if (btnPrev) btnPrev.disabled = data.first;
        if (btnNext) btnNext.disabled = data.last;

    } catch (error) {
        console.error("Erro ao carregar lista:", error);
        const container = document.getElementById("lista-comentarios");
        if (container) {
            container.innerHTML = `<p style="color: var(--danger-color); padding: 20px;">Erro ao carregar dados.</p>`;
        }
    }
}
// Configuração dos botões de paginação
const btnPrev = document.getElementById("prev-page");
if (btnPrev) {
    btnPrev.onclick = () => {
        if (paginaAtual > 0) {
            paginaAtual--;
            carregarPagina();
        }
    };
}

const btnNext = document.getElementById("next-page");
if (btnNext) {
    btnNext.onclick = () => {
        paginaAtual++;
        carregarPagina();
    };
}

// Chama a carga inicial
carregarPagina();

}); // Fecha o document.addEventListener("DOMContentLoaded", ...
