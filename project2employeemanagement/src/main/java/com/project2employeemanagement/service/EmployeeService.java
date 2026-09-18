package com.project2employeemanagement.service;

import com.project2employeemanagement.dto.EmployeeResponseDto;
import com.project2employeemanagement.entity.Department;
import com.project2employeemanagement.entity.Employee;
import com.project2employeemanagement.dto.EmployeeRequestDto;
import com.project2employeemanagement.exception.EmployeeAlreadyExistsException;
import com.project2employeemanagement.exception.EmployeeNotFoundException;
import com.project2employeemanagement.repository.DepartmentRepository;
import com.project2employeemanagement.repository.EmployeeRepository;
import com.project2employeemanagement.mapper.EmployeeMapper;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper mapper;


    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, EmployeeMapper mapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.mapper = mapper;
    }


    @Transactional
    public EmployeeResponseDto create(EmployeeRequestDto dto) {
        String email = normalizeEmail(dto.email());
        String phone = normalizePhone(dto.phone());
        validateUniqueContactInfo(email, phone, null);

        Employee employee = mapper.toEntity(dto);
        employee.setDepartment(resolveDepartment(dto.department()));
        employee.setEmail(email);
        employee.setPhone(phone);
        employee.setActive(true);

        Employee saved = employeeRepository.save(employee);
        return mapper.toDto(saved);
    }


    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> search(String name, String department) {
        String n = (name == null || name.isBlank()) ? null : name.trim();
        String d = (department == null || department.isBlank()) ? null : department.trim();
        List<Employee> results = employeeRepository.search(n, d);
        // Return empty list when no matches; controller will respond 200 with []
        if (results == null || results.isEmpty()) {
            return List.of();
        }
        return results.stream().map(mapper::toDto).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getAll(int page, int size) {

        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }

        Page<Employee> allEmployees = employeeRepository.findAllByActiveTrue(PageRequest.of(page, size));

        return allEmployees.map(mapper::toDto);
    }


    @Transactional(readOnly = true)
    public EmployeeResponseDto getById(Long id) {
        return employeeRepository.findByIdAndActiveTrue(id).map(mapper::toDto).orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
    }


    @Transactional
    public EmployeeResponseDto update(Long id, EmployeeRequestDto dto) {
        Employee existing = employeeRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));

        String email = normalizeEmail(dto.email());
        String phone = normalizePhone(dto.phone());
        validateUniqueContactInfo(email, phone, id);

        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());
        existing.setEmail(email);
        existing.setPhone(phone);
        existing.setDepartment(resolveDepartment(dto.department()));
        existing.setSalary(dto.salary());
        existing.setJoiningDate(dto.joiningDate());
        Employee saved = employeeRepository.save(existing);
        return mapper.toDto(saved);
    }

    private Department resolveDepartment(String departmentName) {
        if (departmentName == null) {
            throw new IllegalArgumentException("Department is required");
        }
        String normalized = departmentName.trim();
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Department name is required");
        }
        return departmentRepository.findByNameIgnoreCase(normalized).orElseGet(() -> departmentRepository.save(new Department(normalized)));
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim();
    }

    private String normalizePhone(String phone) {
        return phone == null ? null : phone.trim();
    }

    private void validateUniqueContactInfo(String email, String phone, Long employeeId) {

        Employee existingEmployee = null;

        if (employeeId != null) {
            existingEmployee = employeeRepository.findById(employeeId).orElse(null);
        }

        // Check email
        if (email != null && employeeRepository.existsByEmailIgnoreCase(email)) {

            if (existingEmployee == null || !email.equalsIgnoreCase(existingEmployee.getEmail())) {

                throw new EmployeeAlreadyExistsException("Employee with this email already exists");
            }
        }

        // Check phone
        if (phone != null && employeeRepository.existsByPhone(phone)) {

            if (existingEmployee == null || !phone.equals(existingEmployee.getPhone())) {

                throw new EmployeeAlreadyExistsException("Employee with this phone number already exists");
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        Employee existing = employeeRepository.findByIdAndActiveTrue(id).orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
        existing.setActive(false);
        employeeRepository.save(existing);
    }

}
