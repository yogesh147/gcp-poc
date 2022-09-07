package com.cloud.google.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.google.dto.EmployeeDto;
import com.cloud.google.dto.EmployeeEducationDto;
import com.cloud.google.enity.Employee;
import com.cloud.google.enity.EmployeeEducation;
import com.cloud.google.exception.RestApiException;
import com.cloud.google.repository.EmployeeEducationRepository;
import com.cloud.google.repository.EmployeeRepository;
import com.cloud.google.utility.AppUtil;
import com.cloud.google.utility.EmployeeResponse;
import com.cloud.google.utility.FileStorage;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeEducationRepository employeeEducationRepository;

	@Autowired
	private FileStorage storage;

	@Override
	public Mono<?> addEmployee(Mono<EmployeeDto> employee) {
		return employee.log().flatMap(emp -> {
			if (employeeRepository.checkEmployeeEmail(emp.getEmpEmail()) > 0) {
				return Mono.error(new RestApiException("Employee Id is already exist", HttpStatus.BAD_REQUEST));
			}
			Employee employeeData = modelMapper.map(emp, Employee.class);
			log.info("Employee Data {}", employeeData);
			try {
				String empPk = AppUtil.INSTANCE.uniqueIdentifier("Emp_");
				employeeData.setEmpId(empPk);
				employeeData.setCreatedOn(LocalDateTime.now().toString());
				employeeData.getEducations().forEach(edu -> {
					edu.setEmpId(empPk);
					edu.setEduId(AppUtil.INSTANCE.uniqueIdentifier("Edu_"));
					edu.setCreatedOn(LocalDateTime.now().toString());
				});
				employeeRepository.save(employeeData);
				return Mono.just(EmployeeResponse.builder()

						.statusCode(HttpStatus.OK.value()).message("Employee successfully added.").build());

			} catch (Exception e) {
				log.info("Exception  {}", e.getLocalizedMessage());
				return Mono.error(new RestApiException(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST));
			}

		});
	}

	@Override
	public Mono<?> updateEmployee(String empId, Mono<EmployeeDto> employee) {
		Optional<Employee> empData = employeeRepository.getEmployee(empId);
		empData.orElseThrow(() -> new RestApiException("Employee Not found", HttpStatus.BAD_REQUEST));

		return employee.log().flatMap(emp -> {

			Employee employeeData = modelMapper.map(emp, Employee.class);
			log.info("Employee Data  {}", employeeData);
			try {

				employeeData.setEmpId(empId);
				employeeData.setModifiedOn(LocalDateTime.now().toString());
				employeeData.setEmpEmail(empData.get().getEmpEmail());
				employeeData.getEducations().clear();
				employeeData.setEducations(empData.get().getEducations());
				employeeRepository.save(employeeData);
				return Mono.just(EmployeeResponse.builder()

						.statusCode(HttpStatus.OK.value()).message("Employee successfully updated.").build());

			} catch (Exception e) {
				log.info("Exception  {}", e.getLocalizedMessage());
				return Mono.error(new RestApiException(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST));
			}
		});
	}

	@Override
	public Mono<?> getAllemployees() {

		List<EmployeeDto> empData = new ArrayList<EmployeeDto>();
		employeeRepository.getAllEmployee().forEach(e -> {
			empData.add(modelMapper.map(e, EmployeeDto.class));
		});
		log.info("Employee Data  {}", empData);
		return Mono.just(EmployeeResponse.builder().statusCode(HttpStatus.OK.value()).message(empData).build());

	}

	@Override
	public Mono<?> searchEmployee(String empId) {
		Optional<Employee> e = employeeRepository.getEmployee(empId);
		e.orElseThrow(() -> new RestApiException("Employee Not found", HttpStatus.BAD_REQUEST));

		log.info("Employee Data  {}", e.get());
		return Mono.just(EmployeeResponse.builder()

				.statusCode(HttpStatus.OK.value()).message(modelMapper.map(e.get(), EmployeeDto.class)).build());
	}

	@Override
	public Mono<?> deleteEmployee(String empId) {
		Optional<Employee> e = employeeRepository.getEmployee(empId);
		e.orElseThrow(() -> new RestApiException("Employee Not found", HttpStatus.BAD_REQUEST));
		try {
			employeeEducationRepository.deleteAll(employeeEducationRepository.getEmployeeEducations(empId));

			employeeRepository.delete(e.get());

			return Mono.just(EmployeeResponse.builder().statusCode(HttpStatus.OK.value())
					.message("Employee is successfully deleted...!").build());
		} catch (Exception e2) {
			log.info("Exception {}", e2.getLocalizedMessage());
			return Mono.error(new RestApiException(e2.getLocalizedMessage(), HttpStatus.BAD_REQUEST));
		}

	}

	@Override
	public Mono<?> updateEmployeeEducationDeatails(String empId, Flux<EmployeeEducationDto> employeeData) {
		Optional<Employee> e = employeeRepository.getEmployee(empId);
		e.orElseThrow(() -> new RestApiException("Employee Not found", HttpStatus.BAD_REQUEST));

		try {
			employeeEducationRepository.deleteAll(employeeEducationRepository.getEmployeeEducations(empId));

			return employeeData.log().map(edu -> {

				EmployeeEducation empEdu = modelMapper.map(edu, EmployeeEducation.class);
				empEdu.setEmpId(empId);
				empEdu.setEduId(AppUtil.INSTANCE.uniqueIdentifier("Edu_"));
				empEdu.setCreatedOn(LocalDateTime.now().toString());
				return empEdu;
			}).collectList().flatMap(edudataList -> {

				employeeEducationRepository.saveAll(edudataList);
				log.info("Employee Data  {}", edudataList);
				return Mono.just(EmployeeResponse.builder()

						.statusCode(HttpStatus.OK.value()).message("Employee is successfully Updaed...!").build());

			});

		} catch (Exception e2) {
			log.info("Exception  {}", e2.getLocalizedMessage());
			return Mono.error(new RestApiException(e2.getLocalizedMessage(), HttpStatus.BAD_REQUEST));
		}
	}

	@Override
	public Mono<?> uploadCertificate(String empId, MultipartFile filePart) {

//		Optional<Employee> emp = employeeRepository.getEmployee(empId);
//		emp.orElseThrow(() -> new RestApiException("Employee Not found", HttpStatus.BAD_REQUEST));

		String imageType = storage.checkFileType(filePart.getOriginalFilename());
		if (StringUtils.isEmpty(imageType)) {
			return Mono.error(new RestApiException("Invalid file.", HttpStatus.BAD_REQUEST));
		}
//		if (!StringUtils.isEmpty(emp.get().getEmpCertificate())) {
//			storage.deleteFile(emp.get().getEmpCertificate());
//		}

		return storage.uploadFiles(filePart, imageType).flatMap(urlInfo -> {
			System.out.println("file name and bucket address :: "
					+ storage.getBucketAddress().concat(filePart.getOriginalFilename()));
//			emp.get().setEmpCertificate(storage.getBucketAddress().concat(filePart.getOriginalFilename()));
//			employeeRepository.save(emp.get());
			return Mono.just(EmployeeResponse.builder().statusCode(HttpStatus.OK.value())
					.message(storage.getBucketAddress().concat(filePart.getOriginalFilename())).build());
		});

	}

	@Override
	public Mono<?> deleteCertificate(String id) {

//		Optional<Employee> emp = employeeRepository.getEmployee(id);
//		emp.orElseThrow(() -> new RestApiException("Employee Not found", HttpStatus.BAD_REQUEST));

//		emp.get().setEmpCertificate("");
//		employeeRepository.save(emp.get());
		return Mono.just(EmployeeResponse.builder()

				.statusCode(HttpStatus.OK.value()).message(storage.deleteFile("bulb.jpg")).build());
	}

}
