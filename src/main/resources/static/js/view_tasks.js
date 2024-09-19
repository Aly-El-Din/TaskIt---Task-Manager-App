document.addEventListener('DOMContentLoaded', function() {
    const tasksContainer = document.getElementById('tasks-container');
    const filterBtn = document.getElementById('filter-btn');
    const statusFilter = document.getElementById('status-filter');  // The select dropdown for status filtering

    // Fetch and display all tasks by default (default filter is 'all')
    fetchTasks('all');

    // Event listener for the filter button
    filterBtn.addEventListener('click', function() {
        const selectedStatus = statusFilter.value;
        fetchTasks(selectedStatus);  // Fetch tasks based on the selected filter
    });

    // Function to fetch tasks based on status (all, complete, or pending)
    function fetchTasks(status = 'all') {
        fetch('/tasks/view', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        .then(response => response.json())
        .then(tasks => {
            tasksContainer.innerHTML = '';  // Clear previous tasks

            if (tasks.length === 0) {
                tasksContainer.innerHTML = '<p>No tasks found.</p>';
            } else {
                // Filter tasks based on the selected status
                const filteredTasks = tasks.filter(task => {
                    return status === 'all' || (task.status.toString() === status);
                });

                if (filteredTasks.length === 0) {
                    tasksContainer.innerHTML = '<p>No tasks found for the selected filter.</p>';
                } else {
                    filteredTasks.forEach(task => {
                        const taskElement = document.createElement('div');
                        taskElement.classList.add('task');
                        taskElement.innerHTML = `
                            <h3>${task.title}</h3>
                            <div class="task-status-container">
                                <input
                                    type="checkbox"
                                    class="task-status-checkbox"
                                    id="status-${task.id}"
                                    ${task.status ? 'checked' : ''}
                                    onclick="toggleTaskStatus(${task.id})"
                                />
                                <label for="status-${task.id}" class="task-status">
                                    ${task.status ? 'Complete' : 'Pending'}
                                </label>
                            </div>
                            <div class="task-icons">
                                <i class="fa fa-eye icon-btn" title="View Details" onclick="viewTaskDetails(${task.id})"></i>
                                <i class="fa fa-pencil icon-btn" title="Edit" onclick="editTask(${task.id})"></i>
                                <i class="fa fa-trash icon-btn" title="Delete" onclick="deleteTask(${task.id})"></i>
                            </div>
                        `;
                        tasksContainer.appendChild(taskElement);
                    });
                }
            }
        })
        .catch(error => {
            console.error('Error fetching tasks:', error);
            tasksContainer.innerHTML = '<p>Error loading tasks.</p>';
        });
    }

    // Event listener for "Create New Task" button
    document.getElementById('create-task-btn').addEventListener('click', function() {
        window.location.href = '/create/task';  // Redirect to create task page
    });
});

function viewTaskDetails(taskId) {
    fetch(`/tasks/${taskId}/details`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => response.json())
    .then(task => {
        localStorage.setItem('selectedTask', JSON.stringify(task));
        window.location.href = `/task/details`;
    })
    .catch(error => {
        console.error('Error fetching task details:', error);
        alert('Failed to fetch task details.');
    });
}


function editTask(taskId) {
    // Fetch task details from the backend for editing
    fetch(`/tasks/${taskId}/details`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => response.json())  // Parse JSON from the response
    .then(task => {
        // Store task details in localStorage, ensure correct format of due_date
        task.due_date = task.due_date ? task.due_date : null;  // Ensure null if not set
        localStorage.setItem('taskToEdit', JSON.stringify(task));

        // Redirect to the edit task page
        window.location.href = `/task/edit`;
    })
    .catch(error => {
        console.error('Error fetching task for edit:', error);
        alert('Failed to fetch task for editing.');
    });
}

function deleteTask(taskId) {
    if (confirm('Are you sure you want to delete this task?')) {
        fetch(`/tasks/${taskId}`, {  // Update URL to match @DeleteMapping path
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        })
        .then(response => {
            if (response.ok) {
                alert('Task deleted');
                window.location.href = '/view/tasks';  // Refresh the page after deletion
            } else {
                throw new Error('Failed to delete task');
            }
        })
        .catch(error => {
            console.error('Error deleting task:', error);
            alert('Failed to delete task');
        });
    }
}

function toggleTaskStatus(taskId) {
    // Send a PUT request to toggle the task status
    fetch(`/tasks/${taskId}/status`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then(response => {
        if (response.ok) {
            // Update the status label and checkbox without expecting a JSON response
            const statusCheckbox = document.getElementById(`status-${taskId}`);
            const statusLabel = document.querySelector(`label[for="status-${taskId}"]`);

            // Toggle the status based on the checkbox state
            const isChecked = statusCheckbox.checked;
            statusLabel.textContent = isChecked ? 'Complete' : 'Pending';
        } else {
            throw new Error('Failed to update task status');
        }
    })
    .catch(error => {
        console.error('Error updating task status:', error);
        alert('Failed to update task status');
    });
}
