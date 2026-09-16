package in.yashKhanpara.Project_2_Employee_Management.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Data transfer object used for employee request and response payloads.
 *
 * <p>This record validates input values for employee creation and update operations
 * and provides conversion helpers between the API layer and the persistence model.</p>
 */
@Schema(description = "Employee Data Transfer Object")
public record EmployeeDto(
        @Schema(description = "Unique identifier", example = "1")
        Long id,

        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50)
        @Schema(description = "First name of the employee", example = "Yash")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50)
        @Schema(description = "Last name of the employee", example = "Khanpara")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        @Schema(description = "Email address", example = "yashkhanpara11@gmail.com")
        String email,

        @NotBlank(message = "Phone is required")
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Phone number must be a valid 10-digit mobile number"
        )
        @Schema(description = "Phone number", example = "8849930662")
        String phone,

        @NotBlank(message = "Department is required")
        @Schema(description = "Department name", example = "Engineering")
        String department,

        @NotNull(message = "Salary is required")
        @DecimalMin(value = "10000", message = "Salary must be at least 10000")
        @DecimalMax(value = "10000000", message = "Salary must not exceed 10000000")
        @Schema(description = "Salary in the company's currency", example = "50000.0")
        Double salary,

        @NotNull(message = "Joining date is required")
        @Schema(description = "Joining date (YYYY-MM-DD)", example = "2026-06-15")
        LocalDate joiningDate,

        @Schema(description = "Active flag indicating whether the employee is active (soft delete)", example = "true")
        Boolean active
) {
    /**
     * Converts this DTO into an {@link Employee} entity.
     *
     * @return a populated employee entity matching the DTO values
     */
    public Employee toEntity() {
        Employee e = new Employee();
        e.setId(this.id);
        e.setFirstName(this.firstName);
        e.setLastName(this.lastName);
        e.setEmail(this.email);
        e.setPhone(this.phone);
        e.setSalary(this.salary);
        e.setJoiningDate(this.joiningDate);
        e.setActive(this.active == null ? Boolean.TRUE : this.active);
        return e;
    }

    /**
     * Converts an {@link Employee} entity into a DTO.
     *
     * @param e the employee entity to convert; may be {@code null}
     * @return the corresponding DTO or {@code null} when the entity is null
     */
    public static EmployeeDto fromEntity(Employee e) {
        if (e == null) return null;
        String departmentName = e.getDepartment() == null ? null : e.getDepartment().getName();
        return new EmployeeDto(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getEmail(),
                e.getPhone(),
                departmentName,
                e.getSalary(),
                e.getJoiningDate(),
                e.getActive()
        );
    }
}