package com.example.TaskManager.Service;

import com.example.TaskManager.Model.AppUser;
import com.example.TaskManager.Model.Task;
import com.example.TaskManager.Repository.AppUserRepository;
import com.example.TaskManager.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AppUserRepository userRepository;

    public List<Task> getUserTasks(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        System.out.println(username+"\n\n");
        // Fetch the user from the database
        Optional<AppUser> appUser = userRepository.findByUsername(username);
        if (appUser.isPresent()) {
            // Return tasks associated with this user
            System.out.println("User is present\n\n");
            List<Task>userTasks=taskRepository.findByAppUser(appUser.get());
            for(Task task:userTasks) System.out.println(task.getTitle());
            System.out.println("\n\n");
            return userTasks;
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public void saveTask(Task newTask){
        taskRepository.save(newTask);
    }
    public Task getTaskById(Long taskId){
        return taskRepository.findById(taskId).orElseThrow();
    }
    public void updateTaskStatus(Long taskId){
        Task task=getTaskById(taskId);
        task.setStatus(!task.getStatus());
        taskRepository.save(task);
    }

    public void editTaskDetails(Task newTask){
        Task updatedTask = getTaskById(newTask.getId());
        updatedTask.setTitle(newTask.getTitle());
        updatedTask.setDescription(newTask.getDescription());
        updatedTask.setStatus(newTask.getStatus());
        updatedTask.setDue_date(newTask.getDue_date());
        taskRepository.save(updatedTask);
    }
    public void deleteTask(Long taskId){
        Task task = getTaskById(taskId);
        taskRepository.delete(task);
    }
}
