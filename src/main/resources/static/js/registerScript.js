document.addEventListener("DOMContentLoaded", function() {
    const signupBtn = document.getElementById("signupBtn");
    const signinBtn = document.getElementById("signinBtn");

    signupBtn.addEventListener('click', () => {
        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        const data = {
            username,
            email,
            password
        };

        const jsonData = JSON.stringify(data);

        console.log(data, jsonData)
        fetch('/req/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonData
        })
        .then(response => {
            if (!response.ok) {
                // If the response is not OK, throw an error
                throw new Error('Network response was not ok');
            }
            return response.json(); // Attempt to parse as JSON
        })
        .then(data => {
            console.log('Success:', data);
            // Optionally redirect or show a message here
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });

    signinBtn.addEventListener('click', () => {
        window.location.href = "/login";
    });
});
