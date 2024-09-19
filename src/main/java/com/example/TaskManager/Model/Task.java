package com.example.TaskManager.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
/** Task entity
* Each user may create some tasks
* and specify some fields of those created tasks such as:
* task title, description, due date, and the status which could be either
* pending or completed.
* In the context of database design, the relationship between the user
* and the tasks is one to many, hence task table should have the username
* of the user who created the task as a foreign key, that's because
* after authentication, the tasks which belong to this user only would be retrieved.
* The primary key of the task is the task id which is generated automatically while
* creating a new task.
 **/

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long task_id;
    private String title;
    private String description;

    private LocalDateTime due_date;
    private boolean status;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="username", nullable = false)
    private AppUser appUser;


    public Task(){}
    public Task(String title, String description, LocalDateTime due_date, boolean status, AppUser appUser){
        this.title=title;
        this.description=description;
        this.due_date=due_date;
        this.status=status;
        this.appUser=appUser;
    }
    public Long getId() {
        return task_id;
    }
    public void setId(Long task_id) {
        this.task_id = task_id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getDue_date() {
        return due_date;
    }
    public void setDue_date(LocalDateTime due_date) {
        this.due_date = due_date;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
