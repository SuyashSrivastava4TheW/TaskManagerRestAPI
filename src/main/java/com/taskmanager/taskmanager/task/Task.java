package com.taskmanager.taskmanager.task;

import com.taskmanager.taskmanager.category.Category;
import com.taskmanager.taskmanager.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Title is necessary")
    private String title;
    private Boolean completed;
    @ManyToOne
    @JoinColumn(name = "Userid")
    private User user;
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    public Task() {
    }

    public Task(String title, Boolean completed) {
        this.title = title;
        this.completed = completed;
    }

}