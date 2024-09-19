package com.example.TaskManager.Repository;

import com.example.TaskManager.Model.AppUser;
import com.example.TaskManager.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAppUser(AppUser appuser);

}
