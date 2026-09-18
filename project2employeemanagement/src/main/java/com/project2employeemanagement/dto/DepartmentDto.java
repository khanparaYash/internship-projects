package com.project2employeemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Department Data Transfer Object")
public record DepartmentDto(
        Long id,
        @NotBlank(message = "Department name is required")
        String name
) {
}
