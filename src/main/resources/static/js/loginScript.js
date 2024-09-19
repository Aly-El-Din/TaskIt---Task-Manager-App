document.addEventListener("DOMContentLoaded", function() {
    let signupBtn = document.getElementById("signupBtnL");
    let signinBtn = document.getElementById("signinBtnL");

    signupBtn.onclick = function() {
        window.location.href = "/register";
    };
});

