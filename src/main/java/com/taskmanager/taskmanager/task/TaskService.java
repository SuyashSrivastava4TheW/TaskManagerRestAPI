package com.taskmanager.taskmanager.task;

import com.taskmanager.taskmanager.category.Category;
import com.taskmanager.taskmanager.category.CategoryNotFoundException;
import com.taskmanager.taskmanager.category.CategoryRepository;
import com.taskmanager.taskmanager.dto.TaskRequest;
import com.taskmanager.taskmanager.dto.TaskResponse;
import com.taskmanager.taskmanager.user.User;
import com.taskmanager.taskmanager.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<TaskResponse> getAllTasks() {
        User user = getAuthenticatedUser();
        List<Task> tasks = taskRepository.findByUser(user);
        return tasks.stream().map(task -> new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getCompleted(),
                task.getCategory() != null ? task.getCategory().getId() : null,
                task.getCategory() != null ? task.getCategory().getName() : null
        )).toList();
    }

    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        task.setUser(getAuthenticatedUser());
        task.setTitle(request.getTitle());
        task.setCompleted(
                request.getCompleted() != null
                        ? request.getCompleted()
                        : false
        );
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() ->
                            new CategoryNotFoundException(request.getCategoryId()));

            task.setCategory(category);
        }
        Task savedTask = taskRepository.save(task);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getCompleted(),
                savedTask.getCategory() != null ? savedTask.getCategory().getId() : null,
                savedTask.getCategory() != null ? savedTask.getCategory().getName() : null
        );
    }

    public TaskResponse getTaskById(Long id) {
        User user =  getAuthenticatedUser();
        Task task = taskRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getCompleted(),
                task.getCategory() != null ? task.getCategory().getId() : null,
                task.getCategory() != null ? task.getCategory().getName() : null
        );
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        User user = getAuthenticatedUser();
        Task task = taskRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.getTitle());

        if (request.getCompleted() != null) {
            task.setCompleted(request.getCompleted());
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(request.getCategoryId()));

            task.setCategory(category);
        } else {
            task.setCategory(null);
        }

        Task updatedTask = taskRepository.save(task);

        return new TaskResponse(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getCompleted(),
                updatedTask.getCategory() != null ? updatedTask.getCategory().getId() : null,
                updatedTask.getCategory() != null ? updatedTask.getCategory().getName() : null
        );
    }

    public void deleteTask(Long id) {
        User user = getAuthenticatedUser();
        Task task = taskRepository.findByIdAndUser(id,user)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
    }
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}