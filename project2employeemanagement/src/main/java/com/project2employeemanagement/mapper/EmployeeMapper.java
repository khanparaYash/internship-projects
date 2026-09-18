package com.project2employeemanagement.mapper;

import com.project2employeemanagement.dto.EmployeeResponseDto;
import com.project2employeemanagement.dto.EmployeeRequestDto;
import com.project2employeemanagement.entity.Department;
import com.project2employeemanagement.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "active", ignore = true)
    Employee toEntity(EmployeeRequestDto dto);

    @Mapping(target = "department", expression = "java(mapDepartmentToName(e.getDepartment()))")
    EmployeeResponseDto toDto(Employee e);

    default String mapDepartmentToName(Department department) {
        return department == null ? null : department.getName();
    }
}
