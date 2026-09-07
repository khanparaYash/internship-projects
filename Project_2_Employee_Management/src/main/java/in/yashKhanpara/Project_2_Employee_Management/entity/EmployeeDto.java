package in.yashKhanpara.Project_2_Employee_Management.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;


@Data
@Schema(description = "Employee Data Transfer Object")
public class EmployeeDto {

    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    @Schema(description = "First name of the employee", example = "Yash")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    @Schema(description = "Last name of the employee", example = "Khanpara")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    @Schema(description = "Email address", example = "yashkhanpara11@gmail.com")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit mobile number"
    )
    @Schema(description = "Phone number", example = "8849930662")
    private String phone;

    @NotBlank(message = "Department is required")
    @Schema(description = "Department name", example = "Engineering")
    private String department;

    @NotNull(message = "Salary is required")
    @DecimalMin(value = "10000", message = "Salary must be at least 10000")
    @DecimalMax(value = "10000000", message = "Salary must not exceed 10000000")
    @Schema(description = "Salary in the company's currency", example = "50000.0")
    private Double salary;

    @NotNull(message = "Joining date is required")
    @Schema(description = "Joining date (YYYY-MM-DD)", example = "2026-06-15")
    private LocalDate joiningDate;


    public Employee toEntity() {
        Employee e = new Employee();
        e.setId(this.id);
        e.setFirstName(this.firstName);
        e.setLastName(this.lastName);
        e.setEmail(this.email);
        e.setPhone(this.phone);
        e.setDepartment(this.department);
        e.setSalary(this.salary);
        e.setJoiningDate(this.joiningDate);
        return e;
    }


    public static EmployeeDto fromEntity(Employee e) {
        if (e == null) return null;
        EmployeeDto dto = new EmployeeDto();
        dto.setId(e.getId());
        dto.setFirstName(e.getFirstName());
        dto.setLastName(e.getLastName());
        dto.setEmail(e.getEmail());
        dto.setPhone(e.getPhone());
        dto.setDepartment(e.getDepartment());
        dto.setSalary(e.getSalary());
        dto.setJoiningDate(e.getJoiningDate());
        return dto;
    }
}