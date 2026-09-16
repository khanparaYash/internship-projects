package in.yashKhanpara.Project_2_Employee_Management.controller;

import in.yashKhanpara.Project_2_Employee_Management.entity.EmployeeDto;
import in.yashKhanpara.Project_2_Employee_Management.service.EmployeeService;
import jakarta.validation.Valid;
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
/**
 * REST controller for managing employee resources.
 *
 * <p>Provides endpoints for creating, retrieving, updating, and deleting employee records.</p>
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Creates a controller with the required employee service dependency.
     *
     * @param employeeService the service used to process employee operations
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Creates a new employee record.
     *
     * @param employeeDto the employee payload to persist
     * @return the created employee resource with HTTP 201 status
     */
    @Operation(summary = "Create a new employee", description = "Create a new employee record")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created", content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto savedEmployee = employeeService.create(employeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    /**
     * Retrieves an employee by the given identifier.
     *
     * @param id the unique employee identifier
     * @return the matching employee resource with HTTP 200 status
     */
    @Operation(summary = "Get employee by id", description = "Retrieve an employee by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@Parameter(description = "Employee id", required = true) @PathVariable Long id) {
        EmployeeDto employee = employeeService.getById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * Lists employees with pagination and summary metadata.
     *
     * @param page zero-based page index
     * @param size number of records per page
     * @return paginated employee response containing content and pagination details
     */
    @Operation(summary = "List employees", description = "Return a paginated list of employees")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = EmployeeDto.class)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEmployees(
            @Parameter(description = "Page index (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        List<EmployeeDto> employees = employeeService.getAll(page, size);
        long totalElements = employeeService.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", employees);
        response.put("page", page);
        response.put("size", size);
        response.put("totalElements", totalElements);
        response.put("totalPages", totalPages);

        return ResponseEntity.ok(response);
    }

    /**
     * Searches employees by optional name (first or last) and/or department.
     * When both parameters are provided, results satisfy both filters (AND).
     *
     * @param name optional partial name to match (first or last)
     * @param department optional department name to filter by
     * @return list of matching employees
     */
    @Operation(summary = "Search employees", description = "Search employees by name (first or last) and/or department")
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDto>> searchEmployees(@Parameter(description = "Partial name to search (first or last)") @RequestParam(required = false) String name,
                                                               @Parameter(description = "Department name to filter by") @RequestParam(required = false) String department) {
        List<EmployeeDto> employees = employeeService.search(name, department);
        return ResponseEntity.ok(employees);
    }

    /**
     * Updates an existing employee record.
     *
     * @param id the identifier of the employee to update
     * @param employeeDto the new employee details
     * @return the updated employee resource with HTTP 200 status
     */
    @Operation(summary = "Update employee", description = "Update an existing employee by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated", content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@Parameter(description = "Employee id", required = true) @PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = employeeService.update(id, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    /**
     * Deletes an employee record by identifier.
     *
     * @param id the employee identifier to delete
     * @return an empty response with HTTP 204 status when deletion succeeds
     */
    @Operation(summary = "Delete employee", description = "Delete an employee by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@Parameter(description = "Employee id", required = true) @PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
