package in.yashKhanpara.Project_2_Employee_Management.entity;
import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * Represents an employee record persisted in the database.
 *
 * <p>This entity stores the core employee details required for the
 * employee management API, including contact information, department,
 * compensation, and joining date.</p>
 */
@Data
@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_employees_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_employees_phone", columnNames = "phone")
        }
)
@Schema(description = "Employee model representing an employee record")
public class Employee {
    @Schema(description = "Unique identifier", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "First name of the employee", example = "Yash")
    private String firstName;

    @Schema(description = "Last name of the employee", example = "Khanpara")
    private String lastName;

    @Schema(description = "Email address", example = "yashkhanpara11@gmail.com")
    @Column(name = "email", nullable = false)
    private String email;

    @Schema(description = "Phone number", example = "8849930662")
    @Column(name = "phone", nullable = false)
    private String phone;

    @Schema(description = "Department assigned to this employee", example = "Engineering")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "department_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_employees_department")
    )
    private Department department;

    @Schema(description = "Salary in the company's currency", example = "50000.0")
    private Double salary;

    @Schema(description = "Joining date (YYYY-MM-DD)", example = "2026-06-15")
    private LocalDate joiningDate;

    @Schema(description = "Active flag indicating whether the employee is active (soft delete)", example = "true")
    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
