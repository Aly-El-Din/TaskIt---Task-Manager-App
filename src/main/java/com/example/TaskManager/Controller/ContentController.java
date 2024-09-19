package com.example.TaskManager.Controller;

import com.example.TaskManager.Model.Task;
import com.example.TaskManager.Service.AppUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/** Content controller
 is responsible for retrieving the name of the html file according to the required redirect
* as an example, when we enter the web app, it's automatically redirected to the login page, if user don't have an account,
* then he clicks on sign up button to be redirected to the register page.
* Each endpoint here seems to retrieve a string, but Thymeleaf would handle that by searching for this string
* in the resources/templates directory to render the html file of the same name of this string.
**/

@Controller
public class ContentController {
    @Autowired
    private AppUserService appUserService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/dashboard")
    public ModelAndView showDashboard() {
        // Get the logged-in user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Get tasks for the user
        List<Task> tasks = appUserService.loadUserTasks(username);

        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("tasks", tasks);
        return modelAndView;
    }
    @GetMapping("/create/task")
    public String createTask(){
        return "create_task";
    }
    @GetMapping("/view/tasks")
    public String viewTasks(){
        return "view_tasks";
    }

    @GetMapping("/task/details")
    public String taskDetailsPage() {
        return "task_details";  // Return the name of your new HTML template
    }

    @GetMapping("/task/edit")
    public String editTaskPage() {
        return "edit_task";  // Return the name of your new HTML template for editing tasks
    }

}
