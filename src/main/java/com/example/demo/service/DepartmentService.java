package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.example.db.mysql.tables.Department;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DepartmentService extends AbstractJooqService {

	
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findAllDepartment() {
        
        List<Map<String, Object>> queryResults = dsl
        		.select()
        		.from(Department.DEPARTMENT)
                .fetch()
                .stream()
                .map(r -> {
                	Map<String, Object> department = convertToMap(r, Department.DEPARTMENT.ID, Department.DEPARTMENT.NAME);
                	
                	return department;
                }).collect(Collectors.toList());
 
        return queryResults;
    }
     
}
