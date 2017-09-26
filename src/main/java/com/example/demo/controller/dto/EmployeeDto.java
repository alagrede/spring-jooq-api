package com.example.demo.controller.dto;

import lombok.Data;

@Data
public class EmployeeDto {

	private Long id;
	private String name;
	private int age;
	private int years;

	private DepartmentDto department;

	public EmployeeDto(Long id, String name, int age, int years, DepartmentDto department) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.years = years;
		this.department = department;
	}

	public EmployeeDto() {
		super();
	}
	
}
