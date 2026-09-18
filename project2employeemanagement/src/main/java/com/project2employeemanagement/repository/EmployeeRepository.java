package com.project2employeemanagement.repository;

import com.project2employeemanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findAllByActiveTrue(Pageable pageable);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhone(String phone);


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
                    LOWER(d.name) LIKE LOWER(CONCAT('%', :department, '%'))
                )
                AND e.active = true
            """)
    List<Employee> search(@Param("name") String name, @Param("department") String department);

    List<Employee> findAllByActiveTrue();

    long countByActiveTrue();

    Optional<Employee> findByIdAndActiveTrue(Long id);
}
