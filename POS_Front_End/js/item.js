// GLOBAL DATA
let itemsData = [];


// SAVE ITEM
function saveItem() {

    let id = $("#item-code").val().trim();
    let name = $("#item-name").val().trim();
    let price = $("#item-price").val().trim();
    let qty = $("#item-qty").val().trim();

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
            getAllItems();
            clearItemForm();
        },

        error: handleAjaxError
    });
}


// UPDATE ITEM
function updateItem() {

    let id = $("#item-code").val().trim();
    let name = $("#item-name").val().trim();
    let price = $("#item-price").val().trim();
    let qty = $("#item-qty").val().trim();

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
            getAllItems();
            clearItemForm();
        },

        error: handleAjaxError
    });
}


// DELETE ITEM
function deleteItem() {

    let id = $("#item-code").val().trim();

    if (!id) {
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
            getAllItems();
            clearItemForm();
        },

        error: handleAjaxError
    });
}


// GET ALL ITEMS
function getAllItems() {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/item",

        success: function (response) {
            itemsData = response.data;
            renderItems(itemsData);
        },

        error: handleAjaxError
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


// RENDER TABLE (for search item)
function renderItems(data) {

    $("#item-table tbody").empty();

    if (!data || data.length === 0) {
        $("#item-table tbody").append(`
            <tr>
                <td colspan="4">No items found</td>
            </tr>
        `);
        return;
    }

    $.each(data, function (index, item) {

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


// SEARCH ITEMS
function searchItems() {

    let keyword = $("#search-box").val().toLowerCase().trim();

    if (keyword === "") {
        renderItems(itemsData);
        return;
    }

    let filtered = itemsData.filter(item =>
        item.id.toLowerCase().includes(keyword) ||
        item.name.toLowerCase().includes(keyword)
    );

    renderItems(filtered);
}

// COMMON ERROR HANDLER (for validation errors @Valid annotation in controller)
function handleAjaxError(error) {

    if (!error.responseJSON) {
        alert("Server error occurred");
        return;
    }

    let response = error.responseJSON;

    // Field validation errors (@Valid)
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

    getAllItems();

    $("#btn-search").click(searchItems);

    $("#search-box").on("keyup", function (e) {
        if (e.key === "Enter") {
            searchItems();
        }
    });

});