const API_URL = "http://localhost:8080";

async function apiGet(path) {
    const response = await fetch(API_URL + path);

    if (!response.ok) {
        throw new Error("Erro " + response.status + " ao acessar " + path);
    }

    return response.json();
}