package com.cloud.google.utility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.gcp.data.spanner.repository.config.EnableSpannerAuditing;
import org.springframework.cloud.gcp.data.spanner.repository.config.EnableSpannerRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cloud.google.enity.Employee;
import com.cloud.google.enity.EmployeeEducation;
import com.cloud.google.repository.EmployeeEducationRepository;
import com.cloud.google.repository.EmployeeRepository;

@Configuration
@EntityScan(basePackageClasses = { EmployeeEducation.class, Employee.class })
@EnableTransactionManagement
@EnableSpannerAuditing
@EnableSpannerRepositories(basePackageClasses = { EmployeeRepository.class, EmployeeEducationRepository.class })
public class AutoConfiguration {

	@Autowired
	private TableCreator spannerTemplateManager;

//	@Bean
	ApplicationRunner applicationRunner() {
		return (args) -> {
			spannerTemplateManager.createTables();
		};
	}
	
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }	
}
