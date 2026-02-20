//  GLOBAL DATA
let cartItems = [];
let itemsData = {};        // Quick lookup for item details
let existingOrderIds = new Set();  // Store existing order IDs


// LOAD CUSTOMERS
function loadCustomers() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/customer",
        success: function (response) {
            let options = "<option value=''>Select Customer</option>";
            $.each(response, function (index, customer) {
                options += `<option value="${customer.id}">${customer.name}</option>`;
            });
            $("#customer-select").html(options);
        },
        error: function (error) {
            alert("Error loading customers");
            console.log(error);
        }
    });
}


// LOAD ITEMS
function loadItems() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/item",
        success: function (response) {

            itemsData = {}; // reset

            let options = "<option value=''>Select Item</option>";

            $.each(response, function (index, item) {

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
            alert("Error loading items");
            console.log(error);
        }
    });
}


// LOAD EXISTING ORDERS
function loadExistingOrders() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/order",
        success: function (response) {
            existingOrderIds.clear();
            $.each(response, function (index, order) {
                existingOrderIds.add(order.orderId);
            });
        },
        error: function (error) {
            console.log("Could not load existing orders:", error);
        }
    });
}


// HELPER FUNCTIONS
function isOrderIdDuplicate(orderId) {
    return existingOrderIds.has(orderId);
}

function generateOrderDetailId(orderId, index) {
    return orderId + "-" + (index + 1);
}


// ADD TO CART
function addToCart() {

    let itemCode = $("#item-select").val();
    let qty = parseInt($("#order-qty").val());
    let orderId = $("#order-id").val();

    if (isOrderIdDuplicate(orderId)) {
        alert("Order ID '" + orderId + "' already exists.");
        return;
    }

    if (!itemCode) {
        alert("Please select an item");
        return;
    }

    if (!qty || qty <= 0) {
        alert("Please enter a valid quantity");
        return;
    }

    if (!orderId) {
        alert("Please enter Order ID first");
        return;
    }

    let item = itemsData[itemCode];

    if (!item) {
        alert("Item not found");
        return;
    }

    let existingItem = cartItems.find(cartItem => cartItem.itemCode === itemCode);
    let currentQtyInCart = existingItem ? existingItem.qty : 0;

    if (currentQtyInCart + qty > item.availableQty) {
        alert("Insufficient stock! Available quantity: " + item.availableQty);
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


function updateCartTable() {

    $("#cart-table tbody").empty();

    let orderId = $("#order-id").val();

    $.each(cartItems, function (index, item) {

        let orderDetailId = generateOrderDetailId(orderId, index);

        $("#cart-table tbody").append(`
            <tr>
                <td>${orderDetailId}</td>
                <td>${item.itemName}</td>
                <td>
                    <input type="number" 
                           min="1" 
                           value="${item.qty}" 
                           class="update-qty" 
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

// Update quantity
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
        alert("Insufficient stock! Available: " + availableQty);
        return;
    }

    cartItems[index].qty = newQty;
    cartItems[index].total = newQty * cartItems[index].unitPrice;

    updateCartTable();
    updateTotalAmount();
});


// Delete item from cart
$(document).on("click", ".btn-delete", function () {

    if (!confirm("Are you sure remove this item?")) {
        return;
    }

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


//  PLACE ORDER
function placeOrder() {

    let orderId = $("#order-id").val();
    let customerId = $("#customer-select").val();

    if (!orderId) {
        alert("Please enter Order ID");
        return;
    }

    if (isOrderIdDuplicate(orderId)) {
        alert("Order ID '" + orderId + "' already exists.");
        return;
    }

    if (!customerId) {
        alert("Please select a customer");
        return;
    }

    if (cartItems.length === 0) {
        alert("Please add items to cart");
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
        success: function () {
            alert("Order Placed Successfully");

            existingOrderIds.add(orderId);

            clearOrderForm();
            loadItems(); // refresh stock
        },
        error: function (error) {
            if (error.responseText) {
                alert(error.responseText);
            } else {
                alert("Error placing order");
            }
        }
    });
}


// CLEAR FORM
function clearOrderForm() {

    $("#order-id").val("");
    $("#customer-select").val("");
    $("#item-select").val("");
    $("#order-qty").val("");

    cartItems = [];

    updateCartTable();
    updateTotalAmount();
}


// INIT
$(document).ready(function () {

    loadCustomers();
    loadItems();
    loadExistingOrders();

    $("#btn-add-to-cart").click(addToCart);
    $("#btn-place-order").click(placeOrder);
});