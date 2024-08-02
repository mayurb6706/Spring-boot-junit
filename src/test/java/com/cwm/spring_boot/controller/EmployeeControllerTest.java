package com.cwm.spring_boot.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cwm.spring_boot.model.Employee;
import com.cwm.spring_boot.service.impl.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

	private final String BASE_URL = "/v1/api/employee";
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeServiceImpl employeeServiceImpl;

	@Autowired
	private ObjectMapper objectMapper;

	private Employee employee1;
	private List<Employee> employees = new ArrayList<Employee>();

	@BeforeEach
	void setUp() throws Exception {

		employee1 = Employee.builder().id(1L).firstName("Mayur").lastName("Bhosale").designation("Software Developer")
				.email("mayur@test.com").build();

		employees.add(employee1);

	}

	// Test for save employee
	@DisplayName("Junit Controller save employee")
	@Test
	void testCreateEmployee_whenSaveEmployee_thenReturnEmployeeObject() throws Exception {

		given(employeeServiceImpl.saveEmployee(any(Employee.class)))
				.willAnswer((invocation) -> invocation.getArgument(0));

		ResultActions response = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee1)));

		response.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.firstName").value("Mayur"))
				.andExpect(jsonPath("$.lastName").value("Bhosale"))
				.andExpect(jsonPath("$.designation").value("Software Developer"))
				.andExpect(jsonPath("$.email").value("mayur@test.com"));

	}
	
	// Test for get all employees
	@DisplayName("Junit for Get all Employee")
	@Test
	public void testGetAllEmployee_whenGetAllEmployee_thenReturnListOfEmployee() throws Exception {
		
		//Give
		given(employeeServiceImpl.getAllEmployees()).willReturn(employees);
		//when
		ResultActions response= this.mockMvc.perform(get(BASE_URL));
		//Then
		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].firstName").value("Mayur"))
		.andExpect(jsonPath("$[1].designation").value("Devops Engineer"));
	}
	
	//Test for Get employee by id
	@DisplayName("Junit for get employee by id 200 Response")
	@Test
	public void testGetEmployeeById_whengetById_thenReturnEmloyeeObject()  throws Exception{
		given(employeeServiceImpl.getEployeeById(employee1.getId())).willReturn(Optional.of(employee1));
		
		ResultActions response=this.mockMvc.perform(get(BASE_URL+"/{id}", employee1.getId()));
		
		response.andDo(print()).andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName").value("Mayur"))
		.andExpect(jsonPath("$.lastName").value("Bhosale"))
		.andExpect(jsonPath("$.email").value("mayur@test.com"))
		.andExpect(jsonPath("$.designation").value("Software Developer"));
	}
	
	//Test for Get employee by id with Not found
		@DisplayName("Junit for get employee by id  400 Response")
		@Test
		public void testGetEmployeeById_whengetById_thenReturnNotFound()  throws Exception{
			given(employeeServiceImpl.getEployeeById(1)).willReturn(Optional.empty());
			
			ResultActions response=this.mockMvc.perform(get(BASE_URL+"/{id}", 1));
			
			response.andDo(print()).andExpect(status().isNotFound());
		}

		
		//Test for Update Employee with ok Status
		@DisplayName("Junit for update employee Ok Status")
		@Test
		public void testUpdateEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
			//Given
			Employee updatedEmployee=Employee.builder()
					.firstName("Mayur")
					.lastName("Bhosale")
					.email("mayur@gmail.com")
					.designation("Devops Engineer")
					.build();
			
			 given(employeeServiceImpl.getEployeeById(employee1.getId())).willReturn(Optional.of(employee1));
	            given(employeeServiceImpl.updateEmployee(any(Employee.class)))
	                    .willAnswer((invocation)-> invocation.getArgument(0));

	            // when -  action or the behaviour that we are going test
	            ResultActions response = mockMvc.perform(put(BASE_URL+"/{id}", employee1.getId())
	                                        .contentType(MediaType.APPLICATION_JSON)
	                                        .content(objectMapper.writeValueAsString(updatedEmployee)));


	            // then - verify the output
	            response.andExpect(status().isOk())
	                    .andDo(print())
	                    .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
	                    .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
	                    .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
		}
		
		//Test for Update Employee with ok Status
		@DisplayName("Junit for update employee Not Found Status")
		@Test
		public void testUpdateEmployee_whenUpdateEmployee_thenReturnNotFound() throws Exception {
			//Given
			Employee updatedEmployee=Employee.builder()
					.firstName("Mayur")
					.lastName("Bhosale")
					.email("mayur@gmail.com")
					.designation("Devops Engineer")
					.build();
			
			 given(employeeServiceImpl.getEployeeById(anyLong())).willReturn(Optional.empty());
	            given(employeeServiceImpl.updateEmployee(any(Employee.class)))
	                    .willAnswer((invocation)-> invocation.getArgument(0));

	            // when -  action or the behaviour that we are going test
	            ResultActions response = mockMvc.perform(put(BASE_URL+"/{id}", employee1.getId())
	                                        .contentType(MediaType.APPLICATION_JSON)
	                                        .content(objectMapper.writeValueAsString(updatedEmployee)));


	            // then - verify the output
	            response.andExpect(status().isNotFound())
	                    .andDo(print());
		}
		
		// Test for Delete Employee
		@DisplayName("Junit for delete Employee ")
		@Test
		public void testDeleteEmployee_whenDeleteEmployeeById_thenDeleteEmployee()  throws Exception{
			// given
			willDoNothing().given(employeeServiceImpl).deleteEmployeeById(1L);
			 // when 
	        ResultActions response = mockMvc.perform(delete(BASE_URL+"/{id}",1L));

	        // then
	        response.andExpect(status().isOk())
	                .andDo(print());
		}
}
