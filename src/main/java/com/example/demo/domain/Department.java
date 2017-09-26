package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Department {

	private @Id @GeneratedValue Long id;
	private String name;

	//@OneToMany(mappedBy="department",cascade={CascadeType.MERGE}, orphanRemoval = false)
	//@Cascade(value = {org.hibernate.annotations.CascadeType.MERGE})
	//private List<Employee> employees = new ArrayList<Employee>();

	
}
