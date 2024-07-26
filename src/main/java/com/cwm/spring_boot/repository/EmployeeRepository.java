package com.cwm.spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cwm.spring_boot.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);
	
	//JPQL find Employee by firstName and lastName
	@Query("select e from Employee e where e.firstName=?1 and e.lastName=?2")
	Employee findByJPQL(String firstname, String lastname);
	
	// JPQL Named Param Employee find by firsName and designation
	@Query("select e from Employee e where e.firstName =:firstname and e.designation=:designation")
	public Employee findByNamedParam(@Param("firstname") String firstname, @Param("designation") String designation);
	
	// Find Employee by Native SQL with index param
	@Query(value = "select * from emp_tab e where e.first_name =?1 and e.last_name=?2", nativeQuery = true)
	Employee findByNativeSqlQueryIndexParam(String firstname, String lastname);
	
	// Find Employee by Native SQL with named param
		@Query(value = "select * from emp_tab e where e.first_name =:firstname and e.designation=:designation", nativeQuery = true)
		Employee findByNativeSqlQueryNamedParam(@Param("firstname") String firstname, @Param("designation") String designation);
}
