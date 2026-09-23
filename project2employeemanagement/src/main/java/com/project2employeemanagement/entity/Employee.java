package com.project2employeemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "employees", uniqueConstraints = {@UniqueConstraint(name = "uk_employees_email", columnNames = "email"), @UniqueConstraint(name = "uk_employees_phone", columnNames = "phone")})
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
    @JoinColumn(name = "department_id", nullable = false, foreignKey = @ForeignKey(name = "fk_employees_department"))
    private Department department;

    @Schema(description = "Salary in the company's currency", example = "50000.0")
    private Double salary;

    @Schema(description = "Joining date (YYYY-MM-DD)", example = "2026-06-15")
    private LocalDate joiningDate;

    @Schema(description = "Active flag indicating whether the employee is active (soft delete)", example = "true")
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDepartment(Department department) {
        if (this.department == department) {
            return;
        }

        Department previousDepartment = this.department;
        this.department = department;

        if (previousDepartment != null && previousDepartment.getEmployees() != null) {
            previousDepartment.getEmployees().remove(this);
        }

        if (department != null && department.getEmployees() != null && !department.getEmployees().contains(this)) {
            department.getEmployees().add(this);
        }
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Department getDepartment() {
        return department;
    }

    public Double getSalary() {
        return salary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public Boolean getActive() {
        return active;
    }
}
