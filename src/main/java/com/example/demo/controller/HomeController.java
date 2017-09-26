package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.exception.ValidationException;

@RestController(value = "/")
public class HomeController {

	@Autowired private EmployeeService employeeService;
	@Autowired private DepartmentService departmentService;
	
	
    @GetMapping
    public ResponseEntity<Page<Map<String, Object>>> get(Pageable pageable, @RequestParam String searchTerm) {
        return new ResponseEntity<>(employeeService.findBySearchTerm(searchTerm, pageable), HttpStatus.OK);
    }
    
    @GetMapping("/findOne")
    public ResponseEntity<Map<String, Object>> getOne(@RequestParam Long id) {
        return new ResponseEntity<>(employeeService.findOne(id), HttpStatus.OK);
    }
    
    @GetMapping("/getAllDepartment")
    public ResponseEntity<List<Map<String, Object>>> getAllDepartment() {
        return new ResponseEntity<>(departmentService.findAllDepartment(), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Wrapper wrapper) {
    	try {
            return new ResponseEntity<>(employeeService.create(wrapper.getProperties()), HttpStatus.CREATED);    		
    	} catch(ValidationException e) {
            return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);    		    		
    	}
    }

    @PutMapping
    public ResponseEntity<?> put(@RequestBody Wrapper wrapper) {
    	try {
            return new ResponseEntity<>(employeeService.update(wrapper.getProperties()), HttpStatus.ACCEPTED);    		
    	} catch(ValidationException e) {
            return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);    		    		
    	}
    }
    
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long id) {
    	employeeService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }
    
}
