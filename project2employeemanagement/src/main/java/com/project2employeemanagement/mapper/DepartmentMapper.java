package com.project2employeemanagement.mapper;

import com.project2employeemanagement.dto.DepartmentDto;
import com.project2employeemanagement.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toEntity(DepartmentDto dto);

    DepartmentDto toDto(Department d);
}
