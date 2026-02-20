// Store cart items
let cartItems = [];
let itemsData = {}; // Store item details for quick lookup
let existingOrderIds = new Set(); // Store existing order IDs

function loadCustomers() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/customer",
        success: function (response) {
            let options = "<option value=''>Select Customer</option>";
            $.each(response, function (index, customer) {
                options += "<option value='" + customer.id + "'>" + customer.name + "</option>";
            });
            $("#customer-select").html(options);
        },
        error: function (error) {
            alert("Error loading customers");
            console.log(error);
        }
    });
}

function loadItems() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/item",
        success: function (response) {
            let options = "<option value=''>Select Item</option>";
            $.each(response, function (index, item) {
                // Store item data for later use
                itemsData[item.id] = {
                    name: item.name,
                    price: parseFloat(item.price),
                    availableQty: parseInt(item.qty)
                };
                options += "<option value='" + item.id + "'>" + item.name + " - Rs." + item.price + "</option>";
            });
            $("#item-select").html(options);
        },
        error: function (error) {
            alert("Error loading items");
            console.log(error);
        }
    });
}

// Load existing orders to track used order IDs
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

// Check if order ID already exists
function isOrderIdDuplicate(orderId) {
    return existingOrderIds.has(orderId);
}

// Generate unique ID for order details
function generateOrderDetailId(orderId, index) {
    return orderId + "-" + (index + 1);
}

function addToCart() {
    let itemCode = $("#item-select").val();
    let qty = parseInt($("#order-qty").val());

    // Validation
    if (!itemCode) {
        alert("Please select an item");
        return;
    }
    if (!qty || qty <= 0) {
        alert("Please enter a valid quantity");
        return;
    }

    let item = itemsData[itemCode];
    if (!item) {
        alert("Item not found");
        return;
    }

    // Check available quantity
    let totalQtyInCart = 0;
    let existingItem = cartItems.find(cartItem => cartItem.itemCode === itemCode);
    if (existingItem) {
        totalQtyInCart = existingItem.qty;
    }

    if (totalQtyInCart + qty > item.availableQty) {
        alert("Insufficient stock! Available quantity: " + item.availableQty);
        return;
    }

    // Check if item already in cart
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

    // Clear inputs
    $("#item-select").val("");
    $("#order-qty").val("");

    // Update cart display
    updateCartTable();
    updateTotalAmount();
}

function updateCartTable() {
    $("#cart-table tbody").html("");
    let orderId = $("#order-id").val() || "N/A";

    $.each(cartItems, function (index, item) {
        $("#cart-table tbody").append(
            "<tr style='cursor: pointer;'>" +
            "<td>" + orderId + "</td>" +
            "<td>" + item.itemName + "</td>" +
            "<td>" + item.qty + "</td>" +
            "<td>Rs. " + item.unitPrice.toFixed(2) + "</td>" +
            "<td>Rs. " + item.total.toFixed(2) + "</td>" +
            "</tr>"
        );
    });

    // Add click event handler to cart table rows
    $("#cart-table tbody tr").on('click', function () {
        let itemName = $(this).find('td:eq(1)').text();
        let qty = $(this).find('td:eq(2)').text();

        fillCartForm(itemName, qty);
    });
}

function fillCartForm(itemName, qty) {
    // Find the item code by name
    let itemCode = null;
    for (let code in itemsData) {
        if (itemsData[code].name === itemName) {
            itemCode = code;
            break;
        }
    }

    if (itemCode) {
        $("#item-select").val(itemCode);
        $("#order-qty").val(qty);
    }
}

function updateTotalAmount() {
    let total = 0;
    $.each(cartItems, function (index, item) {
        total += item.total;
    });
    $("#total-amount").text(total.toFixed(2));
}

function placeOrder() {
    let orderId = $("#order-id").val();
    let customerId = $("#customer-select").val();

    // Validation
    if (!orderId) {
        alert("Please enter Order ID");
        return;
    }

    // Check for duplicate order ID
    if (isOrderIdDuplicate(orderId)) {
        alert("Order ID '" + orderId + "' already exists. Please use a different Order ID.");
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

    // Prepare order details with unique IDs
    let orderDetails = [];
    $.each(cartItems, function (index, item) {
        orderDetails.push({
            id: generateOrderDetailId(orderId, index), // Add unique ID for each order detail
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
            alert("Order Placed Successfully");
            // Add the new order ID to the set
            existingOrderIds.add(orderId);
            // Clear form and cart
            clearOrderForm();
            loadItems(); // Reload items to update quantities
        },
        error: function (error) {
            alert("Error placing order");
            console.log(error);
        }
    });
}

function clearOrderForm() {
    $("#order-id").val("");
    $("#customer-select").val("");
    $("#item-select").val("");
    $("#order-qty").val("");
    cartItems = [];
    updateCartTable();
    updateTotalAmount();
}

$(document).ready(function () {
    loadCustomers();
    loadItems();
    loadExistingOrders(); // Load existing orders on page load

    // Event handlers
    $("#btn-add-to-cart").click(addToCart);
    $("#btn-place-order").click(placeOrder);
});