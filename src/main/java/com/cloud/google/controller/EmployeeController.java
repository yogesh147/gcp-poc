package com.cloud.google.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.google.dto.EmployeeDto;
import com.cloud.google.dto.EmployeeEducationDto;
import com.cloud.google.service.EmployeeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("monibag")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/employee")
    public Mono<?> addEmployee(
    		@Valid @RequestBody EmployeeDto request) {
				return employeeService.addEmployee(Mono.just(request));
    }
    
    @PutMapping(value = "/employee/{id}")
    public Mono<?> updateEmployee(
    	        @PathVariable(value = "id") String empId, @Valid @RequestBody EmployeeDto request) {
				return employeeService.updateEmployee(empId,Mono.just(request));
    }  
    
    
    @PutMapping(value = "/employee/{id}/educations")
    public Mono<?> updateEmployeeEducation(
    	        @PathVariable(value = "id") String empId, @RequestBody List<EmployeeEducationDto> request) {
				return employeeService.updateEmployeeEducationDeatails(empId,Flux.fromIterable(request));
    }      
    
    @GetMapping(value = "/employee/{id}")
    public Mono<?> getEmployee(
    	        @PathVariable(value = "id") String empId) {
				return employeeService.searchEmployee(empId);
    }   
    
    @DeleteMapping(value = "/employee/{id}")
    public Mono<?> deleteEmployee(
    	        @PathVariable(value = "id") String empId) {
    	return employeeService.deleteEmployee(empId);
    }
    
    @GetMapping(value = "/employees")
    public Mono<?> getEmployees() {
    	
			return employeeService.getAllemployees();
    }  
    @PatchMapping(value = "/employee/{id}/certificate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<?> uploadFile(@PathVariable(value = "id") String empId, @RequestParam("files")  MultipartFile  filePart) {
     
    	return employeeService.uploadCertificate(empId, filePart);
    }
    
    @DeleteMapping(value = "/employee/{id}/certificate")
    public Mono<?> deleteFile(
    	        @PathVariable(value = "id") String empId) {
    	return employeeService.deleteCertificate(empId);
    }
}
