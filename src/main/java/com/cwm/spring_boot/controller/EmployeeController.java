package com.cwm.spring_boot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cwm.spring_boot.model.Employee;
import com.cwm.spring_boot.service.impl.EmployeeServiceImpl;

@RestController
@RequestMapping("/v1/api/employee")
public class EmployeeController {

	private EmployeeServiceImpl employeeServiceImpl;

	public EmployeeController(EmployeeServiceImpl employeeServiceImpl) {
		super();
		this.employeeServiceImpl = employeeServiceImpl;
	}

	// Create an Employee
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeServiceImpl.saveEmployee(employee);
	}

	// Get all Employee
	@GetMapping
	public List<Employee> getAllEmployee() {
		return employeeServiceImpl.getAllEmployees();
	}

	// Get employee by employee id
	@GetMapping("{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId) {
		return employeeServiceImpl.getEployeeById(employeeId).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Update employee
	@PutMapping("{id}")
	public ResponseEntity<Employee> updateEmployeeById(@PathVariable("id") long employeeId,
			@RequestBody Employee employee) {
		return employeeServiceImpl.getEployeeById(employeeId).map(existingEmployee -> {
			existingEmployee.setFirstName(employee.getFirstName());
			existingEmployee.setLastName(employee.getLastName());
			existingEmployee.setDesignation(employee.getDesignation());
			existingEmployee.setEmail(employee.getEmail());
			Employee updatedEmployee = employeeServiceImpl.updateEmployee(existingEmployee);
			return ResponseEntity.ok(updatedEmployee);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	//Delete Employee
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") Long employeeId){
	 employeeServiceImpl.deleteEmployeeById(employeeId);
	 return new ResponseEntity<String>("Employee details removed successfully!",HttpStatus.OK);
	}
}
