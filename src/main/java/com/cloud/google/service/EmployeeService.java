package com.cloud.google.service;

import org.springframework.web.multipart.MultipartFile;

import com.cloud.google.dto.EmployeeDto;
import com.cloud.google.dto.EmployeeEducationDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
	public Mono<?> addEmployee(Mono<EmployeeDto> employeeData);

	public Mono<?> updateEmployee(String empId,Mono<EmployeeDto> employeeData);
	
	public Mono<?> updateEmployeeEducationDeatails(String empId,Flux<EmployeeEducationDto> employeeData);
	
	public Mono<?> getAllemployees();

	public Mono<?> searchEmployee(String empId);

	public Mono<?> deleteEmployee(String empId);
	
	public Mono<?> uploadCertificate(String empId,MultipartFile filePart);
	public Mono<?> deleteCertificate(String id);
}
