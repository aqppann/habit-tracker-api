package com.habittracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HabitRequest {
    @NotBlank
    private String habitName;
    private String description;
}
