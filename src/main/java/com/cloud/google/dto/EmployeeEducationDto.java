package com.cloud.google.dto;

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
public class EmployeeEducationDto {

	@JsonProperty(value = "eduId")
	@JsonIgnore
	private String eduId;

	@JsonProperty(value = "qualification")
	private String qualification;

	@JsonProperty(value = "grade")
	private String grade;

	@JsonIgnore
	public void setEduId(String eduId) {
		this.eduId = eduId;
	}
}
