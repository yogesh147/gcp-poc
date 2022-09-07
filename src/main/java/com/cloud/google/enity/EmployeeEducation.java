package com.cloud.google.enity;

import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
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
@Table(name = "empeducations")
public class EmployeeEducation {

	@PrimaryKey(keyOrder = 1)
	@Column(name = "empId")
	private String empId;

	
	@PrimaryKey(keyOrder = 2)
	@Column(name = "eduId")
	private String eduId;

	@Column(name = "qualification")
	private String qualification;

	@Column(name = "grade")
	private String grade;

	@Column(name = "createdOn")
	private String createdOn;

	@Column(name = "modifiedOn")	
	private String modifiedOn;

}
