package com.cwm.spring_boot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.BDDMockito.willDoNothing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cwm.spring_boot.exception.ResourceNotFoundException;
import com.cwm.spring_boot.model.Employee;
import com.cwm.spring_boot.repository.EmployeeRepository;
import com.cwm.spring_boot.service.impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeServiceImpl employeeServiceImpl;

	private Employee employee1;
	private Employee employee2;

	List<Employee> employees = new ArrayList<Employee>();

	@BeforeEach
	public void init() {
//		employeeRepository= Mockito.mock(EmployeeRepository.class);
//		employeeServiceImpl= new EmployeeServiceImpl(employeeRepository);

		employee1 = Employee.builder().id(1L).firstName("Mayur").lastName("Bhosale").designation("Software Developer")
				.email("mayur@test.com").build();
		employee2 = Employee.builder().id(2L).firstName("Akash").lastName("Jadhav").designation("Devops Engineer")
				.email("akash@test.com").build();

		employees.add(employee1);
		employees.add(employee2);
	}

	// Test for Save employee method
	@DisplayName("Junit for Service saveEmployee ")
	@Test
	public void testSaveEmployee_whenSaveEmployee_returnEmployeeObject() {

		// When
		given(employeeRepository.findByEmail(employee1.getEmail())).willReturn(Optional.empty());
		given(employeeRepository.save(employee1)).willReturn(employee1);
		Employee newEmployee = employeeServiceImpl.saveEmployee(employee1);

		// Then
		assertThat(newEmployee).isNotNull();

	}

	// Test for save employee method with Resource not found exception
	@DisplayName("Junit for service save employee with ResourceNotFound exception")
	@Test
	public void testExistingEmail_whenSaveEmployee_thenThrowResourceNotFoundException() {

		// When
		when(employeeRepository.findByEmail(employee1.getEmail())).thenReturn(Optional.of(employee1));
		assertThrows(ResourceNotFoundException.class, () -> {
			employeeServiceImpl.saveEmployee(employee1);
		});

		// then
		verify(employeeRepository, never()).save(any(Employee.class));
	}

	// Test for get all employees
	@DisplayName("Junit for get all employess")
	@Test
	public void testFindAllEmployee_whenGetAllEmployee_thenReturnEmployeeList() {
		// Given
		given(employeeRepository.findAll()).willReturn(employees);
		// when
		List<Employee> list = employeeServiceImpl.getAllEmployees();
		// then
		assertThat(list).isNotEmpty();
		assertThat(list.size()).isEqualTo(2);
	}

	// Test for get all employee with empty list
	@DisplayName("Junit for get all Employee with empty list")
	@Test
	public void givenFindAll_whenGetAllEmployee_thenReturnEmptyList() {
		// Given
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());
		// When
		List<Employee> employees = employeeServiceImpl.getAllEmployees();
		// Then
		assertThat(employees).isEmpty();
		assertThat(employees.size()).isEqualTo(0);
	}

	// Test for get employee by id
	@DisplayName("Junit for get employee by id")
	@Test
	public void givenEmployeeId_whenFindById_thenReturnEmployeeObject() {
		// Given
		given(employeeRepository.findById(employee1.getId())).willReturn(Optional.of(employee1));
		// When
		Optional<Employee> employeeFromDB = employeeServiceImpl.getEployeeById(employee1.getId());
		// Then
		assertThat(employeeFromDB).isNotNull();
		assertThat(employeeFromDB.get().getEmail()).isEqualTo("mayur@test.com");
	}

	// Test for update employee
	@DisplayName("Junit for update employee")
	@Test
	public void testUpdateEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
		// Given
		System.out.println(employee1);
		employee1.setEmail("mayur@gmail.com");
		employee1.setDesignation("Devops Engineer");
		given(employeeRepository.save(employee1)).willReturn(employee1);
		// When
		Employee employeeFromDB = employeeServiceImpl.updateEmployee(employee1);
		System.out.println(employeeFromDB);
		// Then
		assertThat(employeeFromDB).isNotNull();
		assertThat(employeeFromDB.getEmail()).isEqualTo("mayur@gmail.com");
		assertThat(employeeFromDB.getDesignation()).isEqualTo("Devops Engineer");
	}
	
	// Test for delete employee by id
	@DisplayName("Junit for delete employee by id")
	@Test
	public void testDeleteById_whenDeleteEmployee_theDoNoting() {
		
		// Given
		willDoNothing().given(employeeRepository).deleteById(employee1.getId());
		//When
		employeeServiceImpl.deleteEmployeeById(employee1.getId());
		// Then
		verify(employeeRepository,times(1)).deleteById(employee1.getId());
		}
}
