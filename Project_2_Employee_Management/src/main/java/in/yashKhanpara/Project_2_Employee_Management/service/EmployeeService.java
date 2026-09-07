package in.yashKhanpara.Project_2_Employee_Management.service;

import in.yashKhanpara.Project_2_Employee_Management.entity.Employee;
import in.yashKhanpara.Project_2_Employee_Management.entity.EmployeeDto;
import in.yashKhanpara.Project_2_Employee_Management.exception.EmployeeNotFoundException;
import in.yashKhanpara.Project_2_Employee_Management.repository.EmployeeRepository;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;

    }

    public EmployeeDto create(EmployeeDto dto) {
        Employee employee = dto.toEntity();
        Employee saved = repository.save(employee);
        return EmployeeDto.fromEntity(saved);
    }

    public List<EmployeeDto> getAll(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }

        List<Employee> allEmployees = repository.findAll();
        int fromIndex = Math.min(page * size, allEmployees.size());
        int toIndex = Math.min(fromIndex + size, allEmployees.size());
        return allEmployees.subList(fromIndex, toIndex).stream()
                .map(EmployeeDto::fromEntity)
                .collect(Collectors.toList());
    }

    public long count() {
        return repository.findAll().size();
    }

    public EmployeeDto getById(Long id) {
        return repository.findById(id)
                .map(EmployeeDto::fromEntity)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
    }

    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id " + id + " not found"));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setDepartment(dto.getDepartment());
        existing.setSalary(dto.getSalary());
        existing.setJoiningDate(dto.getJoiningDate());
        Employee saved = repository.save(existing);
        return EmployeeDto.fromEntity(saved);
    }

    public void delete(Long id) {
        boolean present = repository.findById(id).isPresent();
        if (!present) {
            throw new EmployeeNotFoundException("Employee with id " + id + " not found");
        }
        repository.deleteById(id);
    }

}
