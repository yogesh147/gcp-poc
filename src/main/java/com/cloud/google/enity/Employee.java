package com.cloud.google.enity;

import java.util.List;

import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Interleaved;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

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
@Table(name = "employee")
public class Employee {

	@PrimaryKey
	@Column(name = "empId")
	private String empId;

	@Column(name = "name")
	private String empName;

	@Column(name = "email")	
	private String empEmail;

	@Column(name = "mobileNo")
	private Long empMobile;

	@Column(name = "createdOn")
	private String createdOn;

	@Column(name = "modifiedOn")	
	private String modifiedOn;

	@Column(name = "empCertificate")	
	private String empCertificate;
	
	@Interleaved
	private List<EmployeeEducation> educations;

}
