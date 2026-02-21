$(document).ready(function () {

    $("#login-form").submit(function (e) {

        e.preventDefault();

        let username = $("#login-username").val().trim();
        let password = $("#login-password").val().trim();

        if (!username || !password) {
            showToast("Please enter username and password", true);
            return;
        }

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/v1/user/login",
            contentType: "application/json",
            data: JSON.stringify({ username, password }),

            success: function (response) {

                if (response.status === 200) {

                    showToast("Login successful");

                    localStorage.setItem("loggedUser", username);

                    // Redirect after 2 seconds
                    setTimeout(function () {
                        window.location.href = "./pages/Home.html";
                    }, 2000);
                }
            },

            error: function (error) {
                handleAjaxError(error);
            }
        });

    });

});



//   COMMON ERROR HANDLER

function handleAjaxError(error) {

    if (!error.responseJSON) {
        showToast("Server error occurred", true);
        return;
    }

    let response = error.responseJSON;

    // Validation errors (@Valid)
    if (typeof response.data === "object" && response.data !== null) {

        let messages = "";

        for (let field in response.data) {
            messages += response.data[field] + "\n";
        }

        showToast(messages.trim(), true);
    }

    // Business exception
    else if (response.message) {
        showToast(response.message, true);
    }

    else {
        showToast("Login failed", true);
    }
}



//   TOAST NOTIFICATION

function showToast(message, isError = false) {

    let toast = $("#toast");

    toast.text(message);

    if (isError) {
        toast.addClass("error");
    } else {
        toast.removeClass("error");
    }

    toast.addClass("show");

    setTimeout(function () {
        toast.removeClass("show");
    }, 2500);
}