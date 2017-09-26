package com.example.demo.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.LikeEscapeStep;
import org.jooq.TableField;
import org.jooq.example.db.mysql.tables.Department;
import org.jooq.example.db.mysql.tables.Employee;
import org.jooq.example.db.mysql.tables.records.EmployeeRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.service.exception.ValidationException;

@Component
public class EmployeeService extends AbstractJooqService {

	public static List<TableField> employeeCreateFields = Arrays.asList(Employee.EMPLOYEE.NAME, Employee.EMPLOYEE.AGE, Employee.EMPLOYEE.YEARS);


    @Transactional(readOnly = true)
    public Map<String, Object> findOne(Long id) {
    	EmployeeRecord record = dsl.selectFrom(Employee.EMPLOYEE).where(Employee.EMPLOYEE.ID.eq(id)).fetchOne();
    	return record.intoMap();
    	//return convertToMap(record, Employee.EMPLOYEE.fields());
    }
	
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> findBySearchTerm(String searchTerm, Pageable pageable) {
        
    	String likeExpression = "%" + searchTerm + "%";
 
        List<Map<String, Object>> queryResults = dsl
        		.select()
        		.from(Employee.EMPLOYEE)
        		.join(Department.DEPARTMENT)
        		.on(Department.DEPARTMENT.ID.eq(Employee.EMPLOYEE.DEPARTMENT_ID))
        		.where(whereClauses(likeExpression))
                
                .orderBy(getSortFields(pageable.getSort(), Employee.EMPLOYEE))
                .limit(pageable.getPageSize()).offset(pageable.getOffset())
                //.fetchInto(EmployeeRecord.class);
                .fetch()
                .stream()
                .map(r -> {
                	Map<String, Object> department = convertToMap(r, Department.DEPARTMENT.ID, Department.DEPARTMENT.NAME);
                	Map<String, Object> employee = convertToMap(r, Employee.EMPLOYEE.ID, Employee.EMPLOYEE.NAME, Employee.EMPLOYEE.AGE, Employee.EMPLOYEE.YEARS);
                	employee.put("department", department);
                	
                	return employee;
                	//DepartmentDto d = new DepartmentDto(r.getValue(Department.DEPARTMENT.ID), r.getValue(Department.DEPARTMENT.NAME));
                	//return new EmployeeDto(r.getValue(Employee.EMPLOYEE.ID), r.getValue(Employee.EMPLOYEE.NAME), r.getValue(Employee.EMPLOYEE.AGE), r.getValue(Employee.EMPLOYEE.YEARS), d);
                }).collect(Collectors.toList());
 
        //List<Map<String, Object>> todoEntries = convertQueryResultsToMap(queryResults);
        long totalCount = findCountByLikeExpression(likeExpression);
 
        return new PageImpl<>(queryResults, pageable, totalCount);
    }
     
    private long findCountByLikeExpression(String likeExpression) {
           return dsl.fetchCount(dsl
        		   .select()
                   .from(Employee.EMPLOYEE)
                   .where(whereClauses(likeExpression))
           );
    }

	private LikeEscapeStep whereClauses(String likeExpression) {
		return Employee.EMPLOYEE.NAME.likeIgnoreCase(likeExpression);
        //.or(Employee.EMPLOYEE.TITLE.likeIgnoreCase(likeExpression))
	}
    
    /*
    protected List<Map<String, Object>> convertQueryResultsToMap(List<EmployeeRecord> queryResults) {
        List<Map<String, Object>> todoEntries = new ArrayList<>();
 
        for (EmployeeRecord queryResult : queryResults) {
            todoEntries.add(queryResult.intoMap());
        }
 
        return todoEntries;
    }
    */

	@Transactional
	public Map<String, Object> create(Map<String, Object> properties) throws ValidationException {
		validate(properties, "Employee", employeeCreateFields);
		return this.createRecord(properties, Employee.EMPLOYEE).intoMap();
	}

	@Transactional
	public Map<String, Object> update(Map<String, Object> properties) throws ValidationException {
		validate(properties, "Employee", Arrays.asList(Employee.EMPLOYEE.ID));
		return this.updateRecord(properties, Employee.EMPLOYEE). intoMap();
	}
	
	@Transactional
	public void delete(Long id) {
		dsl.fetchOne(Employee.EMPLOYEE, Employee.EMPLOYEE.ID.eq(id)).delete();
	}

/*
	@Transactional
	//public void create(long id, String name, String title, int years, Long departmentId) {
	public EmployeeRecord create(com.example.demo.domain.Employee employee) {
		return dsl.insertInto(Employee.EMPLOYEE)
		.set(createRecord(employee)).returning().fetchOne();
		//.set(Employee.EMPLOYEE.ID, id)
		//.set(Employee.EMPLOYEE.NAME, name)
		//.set(Employee.EMPLOYEE.DEPARTMENT_ID, departmentId)
		//.set(Employee.EMPLOYEE.YEARS, years).execute();
	}
	
	private EmployeeRecord createRecord(com.example.demo.domain.Employee employee) {
		EmployeeRecord record = new EmployeeRecord();
		record.setName(employee.getName());
		record.setAge(employee.getAge());
		record.setYears(employee.getYears());
		//record.setDepartmentId(employee.getDepartment().getId());
		return record;
	}
*/}
