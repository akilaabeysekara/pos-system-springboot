// GLOBAL DATA
let itemsData = [];

// SAVE ITEM
function saveItem() {

    let id = $("#item-code").val().trim();
    let name = $("#item-name").val().trim();
    let price = $("#item-price").val().trim();
    let qty = $("#item-qty").val().trim();

    if (id === "" || name === "" || price === "" || qty === "") {
        alert("Please fill all fields");
        return;
    }

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/v1/item",
        contentType: "application/json",
        data: JSON.stringify({
            id,
            name,
            price: parseFloat(price),
            qty: parseInt(qty)
        }),

        success: function (response) {

            alert(response.message);

            if (response.status === 201) {
                getAllItems();
                clearItemForm();
            }
        },

        error: function (error) {

            if (error.responseJSON) {
                alert(error.responseJSON.message);
            } else {
                alert("Error saving item");
            }
        }
    });
}


// UPDATE ITEM
function updateItem() {

    let id = $("#item-code").val().trim();
    let name = $("#item-name").val().trim();
    let price = $("#item-price").val().trim();
    let qty = $("#item-qty").val().trim();

    if (id === "" || name === "" || price === "" || qty === "") {
        alert("Please fill all fields");
        return;
    }

    $.ajax({
        type: "PUT",
        url: "http://localhost:8080/api/v1/item",
        contentType: "application/json",
        data: JSON.stringify({
            id,
            name,
            price: parseFloat(price),
            qty: parseInt(qty)
        }),

        success: function (response) {

            alert(response.message);

            if (response.status === 200) {
                getAllItems();
                clearItemForm();
            }
        },

        error: function (error) {

            if (error.responseJSON) {
                alert(error.responseJSON.message);
            } else {
                alert("Error updating item");
            }
        }
    });
}


// DELETE ITEM
function deleteItem() {

    let id = $("#item-code").val().trim();

    if (id === "") {
        alert("Please select an item");
        return;
    }

    if (!confirm("Are you sure you want to delete this item?")) {
        return;
    }

    $.ajax({
        type: "DELETE",
        url: "http://localhost:8080/api/v1/item/" + id,

        success: function (response) {

            alert(response.message);

            if (response.status === 200) {
                getAllItems();
                clearItemForm();
            }
        },

        error: function (error) {

            if (error.responseJSON) {
                alert(error.responseJSON.message);
            } else {
                alert("Error deleting item");
            }
        }
    });
}


// GET ALL ITEMS
function getAllItems() {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/item",

        success: function (response) {

            itemsData = response.data;

            $("#item-table tbody").empty();

            $.each(response.data, function (index, item) {

                let row = `
                    <tr style="cursor:pointer;">
                        <td>${item.id}</td>
                        <td>${item.name}</td>
                        <td>${item.price}</td>
                        <td>${item.qty}</td>
                    </tr>
                `;

                $("#item-table tbody").append(row);
            });
        }
    });
}


// TABLE CLICK
$(document).on("click", "#item-table tbody tr", function () {

    let id = $(this).find("td:eq(0)").text();
    let name = $(this).find("td:eq(1)").text();
    let price = $(this).find("td:eq(2)").text();
    let qty = $(this).find("td:eq(3)").text();

    fillItemForm(id, name, price, qty);
});


// FILL FORM
function fillItemForm(id, name, price, qty) {

    $("#item-code").val(id);
    $("#item-name").val(name);
    $("#item-price").val(price);
    $("#item-qty").val(qty);
}


// CLEAR FORM
function clearItemForm() {

    $("#item-code").val("");
    $("#item-name").val("");
    $("#item-price").val("");
    $("#item-qty").val("");
}


// INIT
$(document).ready(function () {
    getAllItems();
});