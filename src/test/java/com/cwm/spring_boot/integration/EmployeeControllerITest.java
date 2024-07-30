package com.cwm.spring_boot.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cwm.spring_boot.model.Employee;
import com.cwm.spring_boot.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {

	private final String BASE_URL = "/v1/api/employee";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private EmployeeRepository employeeRepository;

	@BeforeEach
	void setUp() throws Exception {
		employeeRepository.deleteAll();

	}

	// Test for save employee
	
	@DisplayName("Junit Controller save employee")
	@Test
	void testCreateEmployee_whenSaveEmployee_thenReturnEmployeeObject() throws Exception {
		
		Employee newEmployee = Employee.builder().id(1L).firstName("Mayur").lastName("Bhosale")
				.designation("Software Developer").email("mayur@gmail.com").build();
		
		employeeRepository.save(newEmployee);

		ResultActions response = mockMvc.perform(
				post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(newEmployee)));

		response.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.firstName").value("Mayur"))
				.andExpect(jsonPath("$.lastName").value("Bhosale"))
				.andExpect(jsonPath("$.designation").value("Software Developer"))
				.andExpect(jsonPath("$.email").value("mayur@gmail.com"));

	}

	// Test for get all employees
	@DisplayName("Junit for Get all Employee")
	@Test
	public void testGetAllEmployee_whenGetAllEmployee_thenReturnListOfEmployee() throws Exception {
		Employee employee1 = Employee.builder().id(1L).firstName("Mayur").lastName("Bhosale")
				.designation("Software Developer").email("mayur@test.com").build();
		Employee employee2 = Employee.builder().id(2L).firstName("Akash").lastName("Jadhav")
				.designation("Devops Engineer").email("akash@test.com").build();
		List<Employee> employees= new ArrayList<Employee>();
		employees.add(employee1);
		employees.add(employee2);
		employeeRepository.saveAll(employees);
		// Give
		employeeRepository.findAll();
		// when
		ResultActions response = this.mockMvc.perform(get(BASE_URL));
		// Then
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].firstName").value("Mayur"))
				.andExpect(jsonPath("$[1].designation").value("Devops Engineer"));
	}

	// Test for Get employee by id
	@DisplayName("Junit for get employee by id 200 Response")
	@Test
	public void testGetEmployeeById_whengetById_thenReturnEmloyeeObject() throws Exception {
		Employee employee1 = Employee.builder().id(1L).firstName("Mayur").lastName("Bhosale")
				.designation("Software Developer").email("mayur@test.com").build();
		employeeRepository.save(employee1);

		ResultActions response = this.mockMvc.perform(get(BASE_URL + "/{id}", employee1.getId()));

		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("Mayur"))
				.andExpect(jsonPath("$.lastName").value("Bhosale"))
				.andExpect(jsonPath("$.email").value("mayur@test.com"))
				.andExpect(jsonPath("$.designation").value("Software Developer"));
	}

	// Test for Get employee by id with Not found
	@DisplayName("Junit for get employee by id  400 Response")
	@Test
	public void testGetEmployeeById_whengetById_thenReturnNotFound() throws Exception {
		Long searchId = 100L;
		Employee employee1 = Employee.builder().firstName("Mayur").lastName("Bhosale")
				.designation("Software Developer").email("mayur@test.com").build();
		employeeRepository.save(employee1);

		ResultActions response = this.mockMvc.perform(get(BASE_URL + "/{id}", searchId));

		response.andDo(print()).andExpect(status().isNotFound());
	}

	// Test for Update Employee with ok Status
	@DisplayName("Junit for update employee Ok Status")
	@Test
	public void testUpdateEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
		Employee employee1 = Employee.builder().id(1L).firstName("Mayur").lastName("Bhosale")
				.designation("Software Developer").email("mayur@test.com").build();
		employeeRepository.save(employee1);
		// Given
		Employee employee2 = Employee.builder().firstName("Mayur").lastName("Bhosale").email("mayur@gmail.com")
				.designation("Devops Engineer").build();

		// when - action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(put(BASE_URL + "/{id}", employee1.getId())
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(employee2)));

		// then - verify the output
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee2.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee2.getLastName())))
				.andExpect(jsonPath("$.email", is(employee2.getEmail())));
	}

	// Test for Update Employee with ok Status
	@DisplayName("Junit for update employee Not Found Status")
	@Test
	public void testUpdateEmployee_whenUpdateEmployee_thenReturnNotFound() throws Exception {
		// Given
		Long searchId=100L;
		Employee employee1 = Employee.builder().firstName("Mayur").lastName("Bhosale").designation("Software Developer")
				.email("mayur@test.com").build();
		employeeRepository.save(employee1);
		Employee updatedEmployee = Employee.builder().firstName("Mayur").lastName("Bhosale").email("mayur@gmail.com")
				.designation("Devops Engineer").build();

		// when - action or the behaviour that we are going test
		ResultActions response = mockMvc.perform(put(BASE_URL + "/{id}", searchId).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(updatedEmployee)));

		// then - verify the output
		response.andExpect(status().isNotFound()).andDo(print());
	}

	// Test for Delete Employee
	@DisplayName("Junit for delete Employee ")
	@Test
	public void testDeleteEmployee_whenDeleteEmployeeById_thenDeleteEmployee() throws Exception {
		Employee employee1 = Employee.builder().firstName("Mayur").lastName("Bhosale").designation("Software Developer")
				.email("mayur@test.com").build();
		employeeRepository.save(employee1);

		// when
		ResultActions response = mockMvc.perform(delete(BASE_URL + "/{id}", employee1.getId()));

		// then
		response.andExpect(status().isOk()).andDo(print());
	}
}
