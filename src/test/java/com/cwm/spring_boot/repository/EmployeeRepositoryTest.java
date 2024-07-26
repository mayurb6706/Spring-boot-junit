package com.cwm.spring_boot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cwm.spring_boot.model.Employee;

@DataJpaTest
public class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository employeeRepository;

	Employee employee1, employee2;
	List<Employee> employees=new ArrayList<Employee>();
	
	@BeforeEach
	public void init() {
		employee1= Employee.builder().firstName("Mayur").lastName("Bhosale").email("mayur@test.com")
			.designation("Software Developer").build();
		employee2=Employee.builder().firstName("Vaibhav").lastName("Bane").email("vaibhav@test.com")
				.designation("Software Tester").build();
		
		employees.add(employee1);
		employees.add(employee2);
	}
	
	// Test for Save Employee
	@Test
	@DisplayName("Junit for save employee object")
	public void testGivenEmployeeObject_thenReturnSavedEmployeeObjcet() {

		
		// When - Behavior to test
		Employee savedEmployee = employeeRepository.save(employee1);

		// Then - Verify the data
		Assertions.assertThat(savedEmployee).isNotNull();
		Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);

	}

	// Test for get all employees
	@DisplayName("Junit for get all employee")
	@Test
	public void testGivenEmployeeList_whenFindAllCall_thenReturnEmployeeList() {

		// Given pre-condition setup
		employeeRepository.save(employee1);
		employeeRepository.save(employee2);

		// When - Behavior to test
		List<Employee> employees = employeeRepository.findAll();

		// Then Verify the data
		assertThat(employees).isNotEmpty();
		assertThat(employees.size()).isEqualTo(2);
	}

	// Test for get employee by id
	@DisplayName("Junit get employee by id")
	@Test
	public void testGetEmployeeById_whenFindById_theReturnEmployeeObject() {
		// Give
		employeeRepository.save(employee1);

		// When
		Employee employeeFromDB = employeeRepository.findById(employee1.getId()).get();

		// Then
		assertThat(employeeFromDB).isNotNull();
		assertThat(employeeFromDB.getEmail()).isEqualTo("mayur@test.com");
		assertThat(employeeFromDB.getDesignation()).isEqualTo("Software Developer");
		assertThat(employeeFromDB.getFirstName()).isEqualTo("Mayur");

	}

	// Test for update employee
	@DisplayName("Junit for update employee")
	@Test
	public void testUpdateEmployee_whenSaveEmployee_returnUpdatedEmployeeObject() {
		// Given
		employeeRepository.save(employee1);

		// When
		Employee employeeFromDB = employeeRepository.findById(employee1.getId()).get();
		employeeFromDB.setDesignation("Devops Enginner");
		employeeRepository.save(employeeFromDB);
		Employee updatedEmployee = employeeRepository.findById(employeeFromDB.getId()).get();
		System.out.println(updatedEmployee);

		// Then
		assertThat(updatedEmployee).isNotNull();
		assertThat(updatedEmployee.getDesignation()).isEqualTo("Devops Enginner");

	}

	// Test Delete Employee by id
	@DisplayName("Junit for Delete Employee")
	@Test
	public void testDeleteEmployeeById_whenDeleteEmployee_thenDeleteEmployee() {
		// Given
		
		employeeRepository.save(employee1);

		// When
		Employee deleteEmployee = employeeRepository.findById(employee1.getId()).get();
		employeeRepository.delete(deleteEmployee);

		Optional<Employee> employeeFromDB = employeeRepository.findById(deleteEmployee.getId());
		// Then
		assertThat(employeeFromDB).isEmpty();
	}

	// Test get Employee by email
	@DisplayName("Junit for find employee by email")
	@Test
	public void testFindByEmail_whenFindEmployeeByEmail_thenReturnEmployeeObject() {
		// Given
		employeeRepository.save(employee1);

		// When
		Employee employeeFromDB = employeeRepository.findByEmail(employee1.getEmail()).get();

		// Then
		assertThat(employeeFromDB).isNotNull();
		assertThat(employeeFromDB.getEmail()).isEqualTo("mayur@test.com");
		assertThat(employeeFromDB.getDesignation()).isEqualTo("Software Developer");

	}

	// Test get Employee by JPQL Query index param
	@DisplayName("Junit for get employee by JPQL Query index parm")
	@Test
	public void testGetEmployeeByJPQL_whenFindByJPQL_thenReturnEmployeeObject() {
		
		//Given
		employeeRepository.save(employee1);

		// When
				Employee employeeFromDB = employeeRepository.findByJPQL("Mayur", "Bhosale");
		// Then
				assertThat(employeeFromDB).isNotNull();
	}
	
	// Test get Employee By JPQL named Parm 
	@DisplayName("Junit for get Employee by JPQL Param Query Named param")
	@Test
	public void testGetEmployeeByJPQLParam_whenFindByJPQLParam_thenReturnEmployeeObject() {
		
		//Given
				employeeRepository.save(employee1);
		// When
				Employee employeeFromDB = employeeRepository.findByNamedParam("Mayur", "Software Developer");
		// Then
				assertThat(employeeFromDB).isNotNull();

	}
	
	// Test for find employee by firstName and lastName using native SQL query  index param
	@DisplayName("Junit for Native Query with index param")
	@Test
	public void testFindByNativeQuery_whenFindByNativeQuery_thenReturnEmployeeObject() {
		
		//Given
				employeeRepository.save(employee1);
		// When
				Employee employeeFromDB = employeeRepository.findByNativeSqlQueryIndexParam("Mayur", "Bhosale");
		// Then
				assertThat(employeeFromDB).isNotNull();
	}
	
	// Test for find employee by firstName and lastName using native SQL query  index param
		@DisplayName("Junit for Native Query with named param")
		@Test
		public void testFindByNativeQueryNamedParam_whenFindByNativeQuery_thenReturnEmployeeObject() {
			
			//Given
					employeeRepository.save(employee1);
			// When
					Employee employeeFromDB = employeeRepository.findByNativeSqlQueryNamedParam("Mayur", "Software Developer");
			// Then
					assertThat(employeeFromDB).isNotNull();
		}
}
