$(document).ready(function () {

    $("#logout-btn").click(function () {

        let confirmLogout = confirm("Are you sure you want to log out?");

        if (confirmLogout) {

            alert("Logged out successfully");

            localStorage.removeItem("loggedUser");

            window.location.href = "../index.html";
        }

    });

});
