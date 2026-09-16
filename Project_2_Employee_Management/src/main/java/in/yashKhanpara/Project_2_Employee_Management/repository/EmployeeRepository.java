package in.yashKhanpara.Project_2_Employee_Management.repository;

import in.yashKhanpara.Project_2_Employee_Management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for persisting and querying employee records.
 *
 * <p>Extends Spring Data JPA's {@link JpaRepository} to provide CRUD operations
 * and custom existence checks for email and phone uniqueness.</p>
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Checks whether an employee with the same email already exists, ignoring case.
     *
     * @param email the email to search for
     * @return {@code true} if an employee with that email exists; otherwise {@code false}
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Checks whether an employee with the same phone number already exists.
     *
     * @param phone the phone number to search for
     * @return {@code true} if an employee with that phone number exists; otherwise {@code false}
     */
    boolean existsByPhone(String phone);

    /**
     * Searches employees by optional name (matches first or last name, partial & case-insensitive)
     * and optional department (case-insensitive exact match). When both parameters are provided
     * the results satisfy both criteria (logical AND).
     *
     * @param name the partial name to search for, or {@code null} to ignore name filtering
     * @param department the department to filter by, or {@code null} to ignore department filtering
     * @return list of matching employees
     */
    @Query("""
    SELECT e
    FROM Employee e
    JOIN e.department d
    WHERE
        (
            :name IS NULL
            OR
            LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
            OR
            LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
        )
        AND
        (
            :department IS NULL
            OR
            LOWER(d.name) = LOWER(:department)
        )
        AND e.active = true
    """)
    List<Employee> search(@Param("name") String name, @Param("department") String department);

    /**
     * Returns all active employees.
     *
     * @return list of active employees
     */
    List<Employee> findAllByActiveTrue();

    /**
     * Counts active employees.
     *
     * @return number of active employees
     */
    long countByActiveTrue();

    /**
     * Finds an active employee by id.
     *
     * @param id the employee id
     * @return optional active employee
     */
    Optional<Employee> findByIdAndActiveTrue(Long id);
}
