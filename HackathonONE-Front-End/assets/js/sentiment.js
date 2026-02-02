
import { API_URL } from "./api.js";

export async function criarComentario(text) {
    const res = await fetch(`${API_URL}/sentiment`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ text })
    });
    return res.json();
}

export async function atualizarComentario(id, texto) {
    const response = await fetch(`${API_URL}/sentiment/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text: texto }) // Garanta que o nome do campo é 'text' como no seu DTO
    });

    if (response.status === 404) {
        throw new Error("O ID informado não existe no banco de dados.");
    }

    if (!response.ok) {
        const erroData = await response.json();
        throw new Error(erroData[0]?.mensagem || "Erro ao atualizar.");
    }

    return await response.json();
}

export async function uploadCsv(file) {
    const formData = new FormData();
    formData.append("file", file);

    const res = await fetch(`${API_URL}/sentiment/lote`, {
        method: "POST",
        body: formData
    });
    return res.json();
}
