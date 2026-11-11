const API_URL = "http://localhost:8080";

async function request(path, options) {
    const config = options || {};
    const headers = Object.assign({}, config.headers);

    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const response = await fetch(API_URL + path, Object.assign({}, config, { headers: headers }));

    if (response.status === 401 && !path.startsWith("/auth/")) {
        logout();
        throw new Error("Sessão expirada. Faça login novamente.");
    }

    if (response.status === 204) {
        return null;
    }

    const data = await response.json().catch(function () { return null; });

    if (!response.ok) {
        const message = data && data.error ? data.error : "Erro " + response.status;
        throw new Error(message);
    }

    return data;
}

function apiGet(path) {
    return request(path);
}

function apiPost(path, body) {
    return request(path, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
}

function apiDelete(path) {
    return request(path, { method: "DELETE" });
}