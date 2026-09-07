package in.yashKhanpara.Project_2_Employee_Management.entity;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@Schema(description = "Employee model representing an employee record")
public class Employee {
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "First name of the employee", example = "Yash")
    private String firstName;

    @Schema(description = "Last name of the employee", example = "Khanpara")
    private String lastName;

    @Schema(description = "Email address", example = "yashkhanpara11@gmail.com")
    private String email;

    @Schema(description = "Phone number", example = "8849930662")
    private String phone;

    @Schema(description = "Department name", example = "Engineering")
    private String department;

    @Schema(description = "Salary in the company's currency", example = "50000.0")
    private Double salary;

    @Schema(description = "Joining date (YYYY-MM-DD)", example = "2026-06-15")
    private LocalDate joiningDate;

}
