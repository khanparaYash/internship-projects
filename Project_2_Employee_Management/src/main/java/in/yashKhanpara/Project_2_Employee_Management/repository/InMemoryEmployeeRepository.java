package in.yashKhanpara.Project_2_Employee_Management.repository;

import in.yashKhanpara.Project_2_Employee_Management.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository{
    private final Map<Long, Employee> employees = new ConcurrentHashMap<>();
    private Long idCounter = 1L;

    @Override
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(idCounter++);
        }
        employees.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    }

    @Override
    public void deleteById(Long id) {
        employees.remove(id);
    }
}
