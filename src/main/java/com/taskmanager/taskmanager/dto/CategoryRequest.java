package com.taskmanager.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Enter a category name")
        String name
) {
}