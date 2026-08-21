package com.taskmanager.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {

    private Long id;
    private String title;
    private Boolean completed;
    private Long categoryId;
    private String categoryName;
}