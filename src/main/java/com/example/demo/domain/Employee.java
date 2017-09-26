package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Employee {

	private @Id @GeneratedValue Long id;
	private String name;
	private int age;
	private int years;

	@ManyToOne
	private Department department;
	
	private Employee() {}

	public Employee(String name, int age, int years) {
		this.name = name;
		this.age = age;
		this.years = years;
	}
}
