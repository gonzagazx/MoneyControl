const brl = new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" });
const monthInput = document.getElementById("month");
let categoryChart = null;

function currentMonth() {
    const now = new Date();
    const month = String(now.getMonth() + 1).padStart(2, "0");
    return now.getFullYear() + "-" + month;
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

        tr.append(description, category, date, amount);
        tbody.appendChild(tr);
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
        console.error(error);
    }
}

monthInput.value = currentMonth();
monthInput.addEventListener("change", function () {
    loadDashboard(monthInput.value);
});

loadDashboard(monthInput.value);