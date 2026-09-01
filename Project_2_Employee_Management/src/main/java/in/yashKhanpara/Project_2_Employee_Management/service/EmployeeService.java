package in.yashKhanpara.Project_2_Employee_Management.service;

import in.yashKhanpara.Project_2_Employee_Management.entity.Employee;
import in.yashKhanpara.Project_2_Employee_Management.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;

    }

    public Employee create(Employee employee) {
        return repository.save(employee);
    }

    public List<Employee> getAll(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }

        List<Employee> allEmployees = repository.findAll();
        int fromIndex = Math.min(page * size, allEmployees.size());
        int toIndex = Math.min(fromIndex + size, allEmployees.size());
        return allEmployees.subList(fromIndex, toIndex);
    }

    public long count() {
        return repository.findAll().size();
    }

    public Optional<Employee> getById(Long id) {
        return repository.findById(id);
    }

    public Employee update(Long id, Employee employee) {
        Optional<Employee> existing = getById(id);
        if (existing.isPresent()) {
            Employee updatedEmployee = existing.get();
            updatedEmployee.setFirstName(employee.getFirstName());
            updatedEmployee.setLastName(employee.getLastName());
            updatedEmployee.setEmail(employee.getEmail());
            updatedEmployee.setPhone(employee.getPhone());
            updatedEmployee.setDepartment(employee.getDepartment());
            updatedEmployee.setSalary(employee.getSalary());
            updatedEmployee.setJoiningDate(employee.getJoiningDate());
            return repository.save(updatedEmployee);
        }
        return null;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
