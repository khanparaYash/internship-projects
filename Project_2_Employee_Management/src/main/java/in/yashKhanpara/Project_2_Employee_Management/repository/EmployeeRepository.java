package in.yashKhanpara.Project_2_Employee_Management.repository;

import in.yashKhanpara.Project_2_Employee_Management.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee employee);
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    void deleteById(Long id);
}
