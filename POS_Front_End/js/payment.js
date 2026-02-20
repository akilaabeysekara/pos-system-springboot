// GLOBAL DATA
let paymentsData = [];

// LOAD PAYMENTS
function loadPayments() {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/payment",

        success: function (response) {

            paymentsData = response.data;
            renderPayments(paymentsData);
        },

        error: handleAjaxError
    });
}


// RENDER TABLE (for search payment)
function renderPayments(data) {

    $("#payment-table tbody").empty();

    if (!data || data.length === 0) {
        $("#payment-table tbody").append(`
            <tr>
                <td colspan="7">No payment records found</td>
            </tr>
        `);
        return;
    }

    $.each(data, function (index, payment) {

        let amount = payment.amount
            ? parseFloat(payment.amount).toFixed(2)
            : "0.00";

        let formattedDate = payment.dateTime
            ? payment.dateTime.replace("T", " ")
            : "";

        let row = `
            <tr>
                <td>${payment.paymentId}</td>
                <td>${payment.orderId}</td>
                <td>${payment.customerName}</td>
                <td>${payment.itemNames}</td>
                <td>Rs. ${amount}</td>
                <td>${payment.paymentMethod}</td>
                <td>${formattedDate}</td>
            </tr>
        `;

        $("#payment-table tbody").append(row);
    });
}


// SEARCH FUNCTION
function searchPayments() {

    let keyword = $("#search-box").val().toLowerCase().trim();

    if (keyword === "") {
        renderPayments(paymentsData);
        return;
    }

    let filtered = paymentsData.filter(payment =>
        payment.paymentId.toLowerCase().includes(keyword) ||
        payment.orderId.toLowerCase().includes(keyword) ||
        payment.customerName.toLowerCase().includes(keyword)
    );

    renderPayments(filtered);
}

// COMMON ERROR HANDLER
function handleAjaxError(error) {

    if (!error.responseJSON) {
        alert("Server error occurred");
        return;
    }

    let response = error.responseJSON;

    if (typeof response.data === "object" && response.data !== null) {

        let messages = "";

        for (let field in response.data) {
            messages += response.data[field] + "\n";
        }

        alert(messages.trim());

    } else if (response.data) {
        alert(response.data);
    } else {
        alert(response.message);
    }
}

// INIT
$(document).ready(function () {

    loadPayments();

    $("#btn-search").click(searchPayments);

    // Search when pressing Enter
    $("#search-box").on("keyup", function (e) {
        if (e.key === "Enter") {
            searchPayments();
        }
    });
});


