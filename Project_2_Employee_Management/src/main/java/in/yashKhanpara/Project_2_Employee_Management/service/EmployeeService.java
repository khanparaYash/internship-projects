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

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Optional<Employee> getById(Long id) {
        return repository.findById(id);
    }

    public Employee update(Long id, Employee employee) {
        Optional<Employee> existing = getById(id);
        if (existing.isPresent()) {
            Employee updatedEmployee = existing.get();
            updatedEmployee.setName(employee.getName());
            updatedEmployee.setEmail(employee.getEmail());
            updatedEmployee.setDepartment(employee.getDepartment());
            return repository.save(updatedEmployee);
        }
        return null;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
