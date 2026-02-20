//  GLOBAL DATA
let cartItems = [];
let itemsData = {};


//  LOAD NEXT ORDER ID
function loadNextOrderId() {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/order/next-id",

        success: function (response) {
            $("#order-id").text(response.data);
        },

        error: function (error) {
            handleAjaxError(error);
        }
    });
}


//  LOAD CUSTOMERS
function loadCustomers() {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/customer",

        success: function (response) {

            let options = "<option value=''>Select Customer</option>";

            $.each(response.data, function (index, customer) {
                options += `<option value="${customer.id}">
                                ${customer.name}
                            </option>`;
            });

            $("#customer-select").html(options);
        },

        error: function (error) {
            handleAjaxError(error);
        }
    });
}


//  LOAD ITEMS
function loadItems() {

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/item",

        success: function (response) {

            itemsData = {};

            let options = "<option value=''>Select Item</option>";

            $.each(response.data, function (index, item) {

                itemsData[item.id] = {
                    name: item.name,
                    price: parseFloat(item.price),
                    availableQty: parseInt(item.qty)
                };

                options += `<option value="${item.id}">
                                ${item.name} - Rs.${item.price}
                            </option>`;
            });

            $("#item-select").html(options);
        },

        error: function (error) {
            handleAjaxError(error);
        }
    });
}


// HELPER
function generateOrderDetailId(orderId, index) {
    return orderId + "-" + (index + 1);
}


// ADD TO CART
function addToCart() {

    let orderId = $("#order-id").text().trim();
    let itemCode = $("#item-select").val();
    let qty = parseInt($("#order-qty").val());

    if (!orderId) {
        alert("Order ID not generated");
        return;
    }

    if (!itemCode) {
        alert("Please select an item");
        return;
    }

    if (!qty || qty <= 0) {
        alert("Invalid quantity");
        return;
    }

    let item = itemsData[itemCode];

    if (!item) {
        alert("Item not found");
        return;
    }

    let existingItem = cartItems.find(c => c.itemCode === itemCode);
    let currentQty = existingItem ? existingItem.qty : 0;

    if (currentQty + qty > item.availableQty) {
        alert("Insufficient stock. Available: " + item.availableQty);
        return;
    }

    if (existingItem) {
        existingItem.qty += qty;
        existingItem.total = existingItem.qty * existingItem.unitPrice;
    } else {
        cartItems.push({
            itemCode: itemCode,
            itemName: item.name,
            qty: qty,
            unitPrice: item.price,
            total: qty * item.price
        });
    }

    $("#item-select").val("");
    $("#order-qty").val("");

    updateCartTable();
    updateTotalAmount();
}


// UPDATE CART TABLE
function updateCartTable() {

    $("#cart-table tbody").empty();
    let orderId = $("#order-id").text().trim();

    $.each(cartItems, function (index, item) {

        let detailId = generateOrderDetailId(orderId, index);

        $("#cart-table tbody").append(`
            <tr>
                <td>${detailId}</td>
                <td>${item.itemName}</td>
                <td>
                    <input type="number"
                           class="update-qty"
                           min="1"
                           value="${item.qty}"
                           data-index="${index}">
                </td>
                <td>Rs. ${item.unitPrice.toFixed(2)}</td>
                <td>Rs. ${item.total.toFixed(2)}</td>
                <td>
                    <button class="btn-update" data-index="${index}">Update</button>
                    <button class="btn-delete" data-index="${index}">Delete</button>
                </td>
            </tr>
        `);
    });
}


// UPDATE QUANTITY
$(document).on("click", ".btn-update", function () {

    let index = $(this).data("index");
    let newQty = parseInt(
        $(`input.update-qty[data-index='${index}']`).val()
    );

    if (!newQty || newQty <= 0) {
        alert("Invalid quantity");
        return;
    }

    let itemCode = cartItems[index].itemCode;
    let availableQty = itemsData[itemCode].availableQty;

    if (newQty > availableQty) {
        alert("Insufficient stock. Available: " + availableQty);
        return;
    }

    cartItems[index].qty = newQty;
    cartItems[index].total = newQty * cartItems[index].unitPrice;

    updateCartTable();
    updateTotalAmount();
});


// DELETE ITEM
$(document).on("click", ".btn-delete", function () {

    if (!confirm("Remove this item?")) return;

    let index = $(this).data("index");

    cartItems.splice(index, 1);

    updateCartTable();
    updateTotalAmount();
});


// UPDATE TOTAL
function updateTotalAmount() {

    let total = 0;

    $.each(cartItems, function (index, item) {
        total += item.total;
    });

    $("#total-amount").text(total.toFixed(2));
}


// PLACE ORDER
function placeOrder() {

    let orderId = $("#order-id").text().trim();
    let customerId = $("#customer-select").val();

    if (!customerId) {
        alert("Select customer");
        return;
    }

    if (cartItems.length === 0) {
        alert("Cart is empty");
        return;
    }

    let orderDetails = [];

    $.each(cartItems, function (index, item) {

        orderDetails.push({
            id: generateOrderDetailId(orderId, index),
            itemId: item.itemCode,
            qty: item.qty,
            price: item.unitPrice,
            total: item.total
        });
    });

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/v1/order",
        contentType: "application/json",
        data: JSON.stringify({
            orderId: orderId,
            customerId: customerId,
            orderDetails: orderDetails
        }),

        success: function (response) {

            alert(response.message);

            clearOrderForm();
            loadItems();
            loadNextOrderId();
        },

        error: function (error) {
            handleAjaxError(error);
        }
    });
}


// CLEAR FORM
function clearOrderForm() {

    $("#customer-select").val("");
    $("#item-select").val("");
    $("#order-qty").val("");

    cartItems = [];

    updateCartTable();
    updateTotalAmount();
}


// COMMON ERROR HANDLER
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


// INIT
$(document).ready(function () {

    loadNextOrderId();
    loadCustomers();
    loadItems();

    $("#btn-add-to-cart").click(addToCart);
    $("#btn-place-order").click(placeOrder);
});