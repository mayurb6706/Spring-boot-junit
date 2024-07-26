package com.cwm.spring_boot.service;

import java.util.List;
import java.util.Optional;

import com.cwm.spring_boot.model.Employee;

public interface EmployeeService {
	Employee saveEmployee(Employee employee);
	
	List<Employee> getAllEmployees();
	
	Optional<Employee> getEployeeById(long id);
	
	Employee updateEmployee(Employee employee);
	
	void deleteEmployeeById(long id);

}
