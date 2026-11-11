const brl = new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" });
const monthInput = document.getElementById("month");
const messageBox = document.getElementById("message");
let categoryChart = null;

function currentMonth() {
    const now = new Date();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    return now.getFullYear() + "-" + month;
}

function today() {
    const now = new Date();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    const day = String(now.getDate()).padStart(2, "0");
    return now.getFullYear() + "-" + month + "-" + day;
}

function showMessage(text, isError) {
    messageBox.textContent = text;
    messageBox.className = "message " + (isError ? "error" : "success");
}

async function loadSummary(month) {
    const summary = await apiGet("/summary?month=" + month);

    document.getElementById("balance").textContent = brl.format(summary.balance);
    document.getElementById("income").textContent = brl.format(summary.income);
    document.getElementById("expense").textContent = brl.format(summary.expense);
}

async function loadChart(month) {
    const data = await apiGet("/summary/by-category?month=" + month);

    const labels = data.map(function (item) { return item.categoryName; });
    const values = data.map(function (item) { return item.total; });

    if (categoryChart) {
        categoryChart.destroy();
    }

    categoryChart = new Chart(document.getElementById("categoryChart"), {
        type: "doughnut",
        data: {
            labels: labels,
            datasets: [{
                data: values,
                backgroundColor: ["#6366f1", "#ec4899", "#14b8a6", "#f59e0b", "#8b5cf6", "#ef4444"],
                borderWidth: 0
            }]
        },
        options: {
            plugins: {
                legend: { labels: { color: "#e5e7eb" } }
            }
        }
    });
}

async function loadTransactions(month) {
    const all = await apiGet("/transactions");

    const rows = all
        .filter(function (t) { return t.date.startsWith(month); })
        .sort(function (a, b) { return b.date.localeCompare(a.date); });

    const tbody = document.getElementById("transactions");
    tbody.innerHTML = "";

    rows.forEach(function (t) {
        const tr = document.createElement("tr");

        const description = document.createElement("td");
        description.textContent = t.description;

        const category = document.createElement("td");
        category.textContent = t.categoryName;

        const date = document.createElement("td");
        date.textContent = t.date.split("-").reverse().join("/");

        const amount = document.createElement("td");
        const isIncome = t.type === "INCOME";
        amount.textContent = (isIncome ? "+ " : "- ") + brl.format(t.amount);
        amount.className = "right " + (isIncome ? "positive" : "negative");

        const actions = document.createElement("td");
        actions.className = "right";
        const deleteButton = document.createElement("button");
        deleteButton.textContent = "Excluir";
        deleteButton.className = "btn-delete";
        deleteButton.addEventListener("click", function () {
            deleteTransaction(t.id, t.description);
        });
        actions.appendChild(deleteButton);

        tr.append(description, category, date, amount, actions);
        tbody.appendChild(tr);
    });
}

async function loadCategories() {
    const categories = await apiGet("/categories");
    const select = document.getElementById("categorySelect");
    select.innerHTML = "";

    if (categories.length === 0) {
        const option = document.createElement("option");
        option.value = "";
        option.textContent = "Cadastre uma categoria primeiro";
        select.appendChild(option);
        return;
    }

    categories.forEach(function (c) {
        const option = document.createElement("option");
        option.value = c.id;
        option.textContent = c.name + " (" + (c.type === "INCOME" ? "Receita" : "Despesa") + ")";
        select.appendChild(option);
    });
}

async function loadDashboard(month) {
    if (!month) {
        return;
    }

    try {
        await Promise.all([
            loadSummary(month),
            loadChart(month),
            loadTransactions(month)
        ]);
    } catch (error) {
        showMessage("Não foi possível carregar os dados: " + error.message, true);
    }
}

async function deleteTransaction(id, description) {
    if (!confirm("Excluir a transação \"" + description + "\"?")) {
        return;
    }

    try {
        await apiDelete("/transactions/" + id);
        showMessage("Transação excluída.", false);
        loadDashboard(monthInput.value);
    } catch (error) {
        showMessage(error.message, true);
    }
}

document.getElementById("categoryForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const body = {
        name: document.getElementById("categoryName").value.trim(),
        type: document.getElementById("categoryType").value
    };

    try {
        await apiPost("/categories", body);
        event.target.reset();
        showMessage("Categoria cadastrada.", false);
        loadCategories();
    } catch (error) {
        showMessage(error.message, true);
    }
});

document.getElementById("transactionForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const body = {
        description: document.getElementById("description").value.trim(),
        amount: parseFloat(document.getElementById("amount").value),
        date: document.getElementById("date").value,
        categoryId: parseInt(document.getElementById("categorySelect").value, 10)
    };

    try {
        await apiPost("/transactions", body);
        event.target.reset();
        document.getElementById("date").value = today();
        showMessage("Transação cadastrada.", false);
        loadDashboard(monthInput.value);
    } catch (error) {
        showMessage(error.message, true);
    }
});

monthInput.value = currentMonth();
document.getElementById("date").value = today();

monthInput.addEventListener("change", function () {
    loadDashboard(monthInput.value);
});

loadCategories();
loadDashboard(monthInput.value);