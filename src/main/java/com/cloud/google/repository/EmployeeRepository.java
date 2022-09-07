package com.cloud.google.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.gcp.data.spanner.repository.SpannerRepository;
import org.springframework.cloud.gcp.data.spanner.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.cloud.google.enity.Employee;
import com.google.cloud.spanner.Key;

@RepositoryRestResource(collectionResourceRel = "employee", path = "employee")
public interface EmployeeRepository extends SpannerRepository<Employee, Key> {
	 
	@Query("SELECT * FROM employee")
	List<Employee> getAllEmployee();

	@Query("SELECT * from employee where empId = @empId")
	Optional<Employee> getEmployee(@Param("empId") String id);
	
	@Query("SELECT count(email) FROM employee  where  email = @empEmail")
	int checkEmployeeEmail(@Param("empEmail") String id);
}
