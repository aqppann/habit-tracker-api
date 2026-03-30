package com.habittracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HabitResponse {
    private Long id;
    private String name;
    private String description;
}