package in.yashKhanpara.Project_2_Employee_Management.service;

import in.yashKhanpara.Project_2_Employee_Management.entity.Department;
import in.yashKhanpara.Project_2_Employee_Management.entity.Employee;
import in.yashKhanpara.Project_2_Employee_Management.entity.EmployeeDto;
import in.yashKhanpara.Project_2_Employee_Management.exception.EmployeeNotFoundException;
import in.yashKhanpara.Project_2_Employee_Management.repository.DepartmentRepository;
import in.yashKhanpara.Project_2_Employee_Management.repository.EmployeeRepository;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service class responsible for employee management operations.
 *
 * <p>
 * This class encapsulates the business logic for creating, retrieving,
 * updating, and deleting employee records while also normalizing input values.
 * </p>
 */
@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    private final DepartmentRepository departmentRepository;

    /**
     * Creates a service instance backed by the employee repository.
     *
     * @param repository the repository that persists employee entities
     */
    public EmployeeService(EmployeeRepository repository, DepartmentRepository departmentRepository) {
       this.repository = repository;
       this.departmentRepository = departmentRepository;
    }

    /**
     * Creates a new employee using the provided DTO.
     *
     * @param dto the employee details to persist
     * @return the created employee as a DTO
     */
    public EmployeeDto create(EmployeeDto dto) {
        Employee employee = dto.toEntity();

        employee.setDepartment(resolveDepartment(dto.department()));
        employee.setEmail(normalizeEmail(dto.email()));
        employee.setPhone(normalizePhone(dto.phone()));
        employee.setActive(true);

        Employee saved = repository.save(employee);
        return EmployeeDto.fromEntity(saved);
    }

    /**
     * Searches employees by name and/or department. Name matches first or last name
     * using a case-insensitive partial match. Department matches case-insensitively.
     * When both parameters are provided results satisfy both filters (AND).
     *
     * @param name the partial name to search for, or null/blank to ignore
     * @param department the department to filter by, or null/blank to ignore
     * @return list of matching employee DTOs
     */
    public List<EmployeeDto> search(String name, String department) {
        String n = (name == null || name.isBlank()) ? null : name.trim();
        String d = (department == null || department.isBlank()) ? null : department.trim();
        List<Employee> results = repository.search(n, d);
        // Return empty list when no matches; controller will respond 200 with []
        if (results == null || results.isEmpty()) {
            return List.of();
        }
        return results.stream().map(EmployeeDto::fromEntity).collect(Collectors.toList());
    }

    /**
     * Retrieves a paginated subset of employees from the repository.
     *
     * @param page the zero-based page index
     * @param size the number of employees to include per page
     * @return a list of employees for the requested page
     * @throws IllegalArgumentException if the page index is negative or page size is not positive
     */
    public List<EmployeeDto> getAll(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }

        List<Employee> allEmployees = repository.findAllByActiveTrue();
        int fromIndex = Math.min(page * size, allEmployees.size());
        int toIndex = Math.min(fromIndex + size, allEmployees.size());
        return allEmployees.subList(fromIndex, toIndex).stream()
                .map(EmployeeDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Counts all persisted employees.
     *
     * @return the total number of employees in storage
     */
    public long count() {
        return repository.countByActiveTrue();
    }

    /**
     * Finds an employee by its identifier.
     *
     * @param id the identifier of the employee to fetch
     * @return the matching employee DTO
     * @throws EmployeeNotFoundException if no employee exists for the requested identifier
     */
    public EmployeeDto getById(Long id) {
        return repository.findByIdAndActiveTrue(id)
                .map(EmployeeDto::fromEntity)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
    }

    /**
     * Updates an existing employee record with new details.
     *
     * @param id the employee identifier to update
     * @param dto the updated employee information
     * @return the saved employee DTO after the update
     * @throws EmployeeNotFoundException if the employee does not exist
     */
    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee existing = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));

        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());
        existing.setEmail(normalizeEmail(dto.email()));
        existing.setPhone(normalizePhone(dto.phone()));
       existing.setDepartment(resolveDepartment(dto.department()));
        existing.setSalary(dto.salary());
        existing.setJoiningDate(dto.joiningDate());
        Employee saved = repository.save(existing);
        return EmployeeDto.fromEntity(saved);
    }

    private Department resolveDepartment(String departmentName) {
       String normalized = departmentName == null ? null : departmentName.trim();
       if (normalized == null || normalized.isBlank()) {
           throw new IllegalArgumentException("Department is required");
       }
       return departmentRepository.findByNameIgnoreCase(normalized)
               .orElseGet(() -> departmentRepository.save(new Department(normalized)));
    }

    /**
     * Trims leading and trailing whitespace from an email value.
     *
     * @param email the email to normalize
     * @return the trimmed email or {@code null} when the input is null
     */
    private String normalizeEmail(String email) {
        return email == null ? null : email.trim();
    }

    /**
     * Trims leading and trailing whitespace from a phone number.
     *
     * @param phone the phone number to normalize
     * @return the trimmed phone number or {@code null} when the input is null
     */
    private String normalizePhone(String phone) {
        return phone == null ? null : phone.trim();
    }

    /**
     * Deletes an employee by identifier.
     *
     * @param id the employee identifier to remove
     * @throws EmployeeNotFoundException if no matching employee exists
     */
    public void delete(Long id) {
        Employee existing = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
        existing.setActive(false);
        repository.save(existing);
    }

}
