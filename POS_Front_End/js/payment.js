$(document).ready(function () {
    loadPayments();
});

function loadPayments() {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/payment",

        success: function (response) {

            $("#payment-table tbody").empty();

            $.each(response.data, function (index, payment) {

                let row = `
                    <tr>
                        <td>${payment.paymentId}</td>
                        <td>${payment.orderId}</td>
                        <td>${payment.customerName}</td>
                        <td>${payment.itemNames}</td>
                        <td>Rs. ${payment.amount.toFixed(2)}</td>
                        <td>${payment.paymentMethod}</td>
                        <td>${payment.dateTime.replace("T"," ")}</td>
                    </tr>
                `;

                $("#payment-table tbody").append(row);
            });
        },

        error: function (error) {
            handleAjaxError(error);
        }
    });
}


function handleAjaxError(error) {

    if (error.responseJSON) {

        if (error.responseJSON.data) {
            alert(error.responseJSON.data);
        } else {
            alert(error.responseJSON.message);
        }

    } else {
        alert("Server error");
    }
}