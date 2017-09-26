package com.example.demo.controller.dto;

import lombok.Data;

@Data
public class DepartmentDto {

	private Long id;
	private String name;
	
	public DepartmentDto(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public DepartmentDto() {
		super();
	}
	
}
