package com.project2employeemanagement.controller;

import com.project2employeemanagement.dto.EmployeeResponseDto;
import com.project2employeemanagement.dto.EmployeeRequestDto;
import com.project2employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Create a new employee", description = "Create a new employee record")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Employee created", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))), @ApiResponse(responseCode = "400", description = "Invalid input")})
    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto employeeDto) {
        EmployeeResponseDto savedEmployee = employeeService.create(employeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @Operation(summary = "Get employee by id", description = "Retrieve an employee by its id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))), @ApiResponse(responseCode = "404", description = "Not Found")})
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@Parameter(description = "Employee id", required = true) @PathVariable Long id) {
        EmployeeResponseDto employee = employeeService.getById(id);
        return ResponseEntity.ok(employee);
    }


    @Operation(summary = "List employees", description = "Return a paginated list of employees")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class)))})
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEmployees(@Parameter(description = "Page index (zero-based)") @RequestParam(defaultValue = "0") int page, @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Page<EmployeeResponseDto> employees = employeeService.getAll(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", employees.getContent());
        response.put("page", employees.getNumber());
        response.put("size", employees.getSize());
        response.put("totalElements", employees.getTotalElements());
        response.put("totalPages", employees.getTotalPages());

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Search employees", description = "Search employees by name (first or last) and/or department")
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseDto>> searchEmployees(@Parameter(description = "Partial name to search (first or last)") @RequestParam(required = false) String name, @Parameter(description = "Department name to filter by") @RequestParam(required = false) String department) {
        List<EmployeeResponseDto> employees = employeeService.search(name, department);
        return ResponseEntity.ok(employees);
    }


    @Operation(summary = "Update employee", description = "Update an existing employee by id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Employee updated", content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))), @ApiResponse(responseCode = "404", description = "Not Found")})
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@Parameter(description = "Employee id", required = true) @PathVariable Long id, @Valid @RequestBody EmployeeRequestDto employeeDto) {
        EmployeeResponseDto updatedEmployee = employeeService.update(id, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }


    @Operation(summary = "Delete employee", description = "Delete an employee by id")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Deleted"), @ApiResponse(responseCode = "404", description = "Not Found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@Parameter(description = "Employee id", required = true) @PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
