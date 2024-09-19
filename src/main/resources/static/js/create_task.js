document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission

        const formData = new FormData(form);
        const task = {
            title: formData.get('title'),
            description: formData.get('description'),
            due_date: formData.get('due_date'),
            status: formData.get('status') === 'true' // Convert status to boolean
        };

        fetch('/tasks/task', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(task),
        })
        .then(response => {
            if (response.ok) {
                window.location.href = '/dashboard'; // Redirect on success
            } else {
                alert('Error creating task');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error creating task');
        });
    });
});
