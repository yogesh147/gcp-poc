package com.cloud.google.utility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeResponse extends APIResponse {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("statusCode")
	private int statusCode;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("statusMessage")
	private Object message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("errorMessage")
	private Object errors;

}