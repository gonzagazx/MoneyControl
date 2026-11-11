const form = document.getElementById("authForm");
const nameInput = document.getElementById("name");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const submitButton = document.getElementById("submitButton");
const toggleButton = document.getElementById("toggleButton");
const toggleText = document.getElementById("toggleText");
const subtitle = document.getElementById("subtitle");
const messageBox = document.getElementById("message");

let mode = "login";

if (getToken()) {
    window.location.href = "index.html";
}

function showMessage(text, isError) {
    messageBox.textContent = text;
    messageBox.className = "message " + (isError ? "error" : "success");
}

function setMode(newMode) {
    mode = newMode;
    messageBox.textContent = "";

    if (mode === "login") {
        subtitle.textContent = "Entre na sua conta";
        submitButton.textContent = "Entrar";
        toggleText.textContent = "Não tem conta?";
        toggleButton.textContent = "Cadastre-se";
        nameInput.hidden = true;
        nameInput.required = false;
        passwordInput.autocomplete = "current-password";
    } else {
        subtitle.textContent = "Crie sua conta";
        submitButton.textContent = "Cadastrar";
        toggleText.textContent = "Já tem conta?";
        toggleButton.textContent = "Entrar";
        nameInput.hidden = false;
        nameInput.required = true;
        passwordInput.autocomplete = "new-password";
    }
}

toggleButton.addEventListener("click", function () {
    setMode(mode === "login" ? "register" : "login");
});

form.addEventListener("submit", async function (event) {
    event.preventDefault();

    const path = mode === "login" ? "/auth/login" : "/auth/register";
    const body = {
        email: emailInput.value.trim(),
        password: passwordInput.value
    };

    if (mode === "register") {
        body.name = nameInput.value.trim();
    }

    submitButton.disabled = true;

    try {
        const data = await apiPost(path, body);
        saveSession(data.token, data.name);
        window.location.href = "index.html";
    } catch (error) {
        showMessage(error.message, true);
        submitButton.disabled = false;
    }
});