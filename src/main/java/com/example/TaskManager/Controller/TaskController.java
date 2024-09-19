package com.example.TaskManager.Controller;

import ch.qos.logback.core.model.Model;
import com.example.TaskManager.Model.AppUser;
import com.example.TaskManager.Model.Task;
import com.example.TaskManager.Service.AppUserService;
import com.example.TaskManager.Service.TaskService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private AppUserService appUserService;

    @PostMapping("/task")
    public String submitTask(@RequestBody Task task){
        AppUser appUser=getUser();
        Task newTask = new Task(task.getTitle(),task.getDescription(),task.getDue_date(),task.getStatus(),appUser);
        taskService.saveTask(newTask);
        return "redirect:/dashboard";
    }
    @GetMapping("/view")
    @ResponseBody
    public List<Task> viewTasks(){
        System.out.println("\n\nconnected\n\n");
        return taskService.getUserTasks();
    }
    @GetMapping("/{taskId}/details")
    @ResponseBody
    public Task viewTaskDetails(@PathVariable Long taskId){
        return taskService.getTaskById(taskId);
    }
    @PutMapping("/{taskId}/status")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable Long taskId) {
        System.out.println("\n\nConnected " + taskId + "\n\n");
        taskService.updateTaskStatus(taskId);
        return ResponseEntity.noContent().build();  // No content returned, just a 204 status code
    }
    @PutMapping("/task")
    @ResponseBody
    public void editTaskDetails(@RequestBody Task newTask){
        System.out.println("\n\nReceived task for update: " + newTask.getDue_date()+"\n\n");
        taskService.editTaskDetails(newTask);
    }
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        try {
            taskService.deleteTask(taskId);
            // Return no content (204) or a redirect to the view tasks page
            return ResponseEntity.noContent().build();  // OR, return redirect if using Thymeleaf views:
            // return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/view/tasks")).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    private AppUser getUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        AppUser appUser= appUserService.getAppUserByUsername(username);
        return appUser;
    }
}
