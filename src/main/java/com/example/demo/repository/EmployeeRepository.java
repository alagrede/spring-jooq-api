package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Override
    void delete(Long aLong);

}
