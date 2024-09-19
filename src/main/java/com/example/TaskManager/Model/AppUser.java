package com.example.TaskManager.Model;

import jakarta.persistence.*;
import java.util.List;
/** App user entity
* any new user of the app should register first his/her credentials:
* username, email, password.
* each user would have his created tasks and have the access to view, update, create new, delete, change status of
* any task.
* */
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task>tasks;

    public void setId(Long id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public Long getId() {return id;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public List<Task>getTasks(){return tasks;}
    public void setTasks(List<Task>tasks){this.tasks=tasks;}
}
