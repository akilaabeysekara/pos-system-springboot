// GLOBAL DATA
let customersData = [];

// SAVE CUSTOMER
function saveCustomer() {

    let id = $('#customer_id').val().trim();
    let name = $('#customer_name').val().trim();
    let address = $('#customer_address').val().trim();
    let phone = $('#customer_phone').val().trim();

    $.ajax({
        url: 'http://localhost:8080/api/v1/customer',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ id, name, address, phone }),

        success: function (response) {

            alert(response.message);

            getALLCustomers();
            clearCustomerForm();
        },

        error: handleAjaxError
    });
}


// UPDATE CUSTOMER
function updateCustomer() {

    let id = $('#customer_id').val().trim();
    let name = $('#customer_name').val().trim();
    let address = $('#customer_address').val().trim();
    let phone = $('#customer_phone').val().trim();

    $.ajax({
        url: 'http://localhost:8080/api/v1/customer',
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({ id, name, address, phone }),

        success: function (response) {

            alert(response.message);

            getALLCustomers();
            clearCustomerForm();
        },

        error: handleAjaxError
    });
}


// DELETE CUSTOMER
function deleteCustomer() {

    let id = $('#customer_id').val().trim();

    if (!id) {
        alert("Please select a customer");
        return;
    }

    if (!confirm("Are you sure you want to delete this customer?")) {
        return;
    }

    $.ajax({
        url: 'http://localhost:8080/api/v1/customer/' + id,
        method: 'DELETE',

        success: function (response) {

            alert(response.message);

            getALLCustomers();
            clearCustomerForm();
        },

        error: handleAjaxError
    });
}


// GET ALL CUSTOMERS
function getALLCustomers() {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/customer",

        success: function (response) {

            customersData = response.data;

            $("#customer-table tbody").empty();

            $.each(response.data, function (index, customer) {

                let row = `
                    <tr style="cursor:pointer;">
                        <td>${customer.id}</td>
                        <td>${customer.name}</td>
                        <td>${customer.address}</td>
                        <td>${customer.phone}</td>
                    </tr>
                `;

                $("#customer-table tbody").append(row);
            });
        },

        error: handleAjaxError
    });
}


// TABLE ROW CLICK
$(document).on("click", "#customer-table tbody tr", function () {

    let id = $(this).find("td:eq(0)").text();
    let name = $(this).find("td:eq(1)").text();
    let address = $(this).find("td:eq(2)").text();
    let phone = $(this).find("td:eq(3)").text();

    fillCustomerForm(id, name, address, phone);
});


// FILL FORM
function fillCustomerForm(id, name, address, phone) {

    $('#customer_id').val(id);
    $('#customer_name').val(name);
    $('#customer_address').val(address);
    $('#customer_phone').val(phone);
}


// CLEAR FORM
function clearCustomerForm() {

    $('#customer_id').val('');
    $('#customer_name').val('');
    $('#customer_address').val('');
    $('#customer_phone').val('');
}

// COMMON ERROR HANDLER
function handleAjaxError(error) {

    if (!error.responseJSON) {
        alert("Server error occurred");
        return;
    }

    let response = error.responseJSON;

    // Validation errors (field-level)
    if (typeof response.data === "object" && response.data !== null) {

        let messages = "";

        for (let field in response.data) {
            messages += response.data[field] + "\n";
        }

        alert(messages.trim());

    } else if (response.data) {
        // Business exception message
        alert(response.data);

    } else {
        alert(response.message);
    }
}

// INIT
$(document).ready(function () {
    getALLCustomers();
});