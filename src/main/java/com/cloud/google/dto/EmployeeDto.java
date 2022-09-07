package com.cloud.google.dto;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto {

	@JsonProperty(value = "empId")
	@JsonIgnore
	private String empId;
	
	
	@JsonProperty(value = "empCertificate")
	@JsonIgnore
	private String empCertificate;	
	
	@JsonProperty(value = "name")
	@NotEmpty(message = "Employee Name is mandatory")
	@NotNull(message = "Employee Name is mandatory")
	private String empName;

	@JsonProperty(value = "email")
	@Email(message = "Valid email is required")
	private String empEmail;

	@JsonProperty(value = "mobile")
	private Long empMobile;

	@JsonProperty(value = "qualifications")
	private List<EmployeeEducationDto> educations;
	
	@JsonIgnore
	public void setEmpId(String empId)
	{
		this.empId=empId;
	}
	
	@JsonIgnore
	public void setEmpCertificate(String empCertificate)
	{
		this.empCertificate=empCertificate;
	}	

}
