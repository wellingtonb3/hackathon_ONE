
import { API_URL } from "./api.js"; // <--- ADICIONE ESTA LINHA NO TOPO

export async function buscarComentario(id) {
    const res = await fetch(`${API_URL}/sentiment/${id}`);
    return res.json();
}

export async function deletarComentario(id) {
    const res = await fetch(`${API_URL}/sentiment/${id}`, {
        method: "DELETE"
    });
    return res.json();
}

export async function listarComentarios(page = 0, size = 10) {
    const response = await fetch(`${API_URL}/sentiment?page=${page}&size=${size}`);
    return response.json();
}