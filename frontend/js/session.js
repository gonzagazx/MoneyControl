const TOKEN_KEY = "moneycontrol_token";
const NAME_KEY = "moneycontrol_name";

function saveSession(token, name) {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(NAME_KEY, name);
}

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function getUserName() {
    return localStorage.getItem(NAME_KEY) || "";
}

function clearSession() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(NAME_KEY);
}

function logout() {
    clearSession();
    window.location.href = "login.html";
}

function requireLogin() {
    if (!getToken()) {
        window.location.href = "login.html";
    }
}