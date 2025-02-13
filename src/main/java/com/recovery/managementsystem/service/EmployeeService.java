package com.recovery.managementsystem.service;

import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.validation.Valid;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private FileUploadService fileUploadService;

	public EmployeeService(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}

	public Employee validateUser(String employeeId, String password) {
		Employee employee = employeeRepository.findByEmployeeId(employeeId);
		if (employee == null) {
			throw new RuntimeException("Employee ID not found");
		}
		if (!passwordEncoder.matches(password, employee.getPassword())) {
			throw new RuntimeException("Password is not correct");
		}

		return employee;
	}

	public String generateReferralCode() {
		String baseCode = "AR111";
		int increment = 4;

		// Fetch the last user
		Employee lastUser = employeeRepository.findTopByOrderByIdDesc();

		if (lastUser != null && lastUser.getEmployeeId() != null) {
			String lastReferralCode = lastUser.getEmployeeId();
			int lastDigits = Integer.parseInt(lastReferralCode.substring(5));

			// Increment without worrying about a limit
			int newDigits = lastDigits + increment;

			return baseCode + String.format("%04d", newDigits);
		}

		// If no users exist, start from the initial value
		return baseCode + "0004";
	}

	public String registerUser(Employee employee, String panFilePath, String aadhaarFilePath) {
		String employeeID = generateReferralCode();
		employee.setEmployeeId(employeeID);
		employee.setPassword(passwordEncoder.encode(employee.getPassword()));
		employee.setRole("USER");
		employee.setEmployeeName(employee.getEmployeeName());
		employee.setAccountNumber(employee.getAccountNumber());
		employee.setAddress(employee.getAddress());
		employee.setAdhaarCard(aadhaarFilePath);
		employee.setBankName(employee.getBankName());
		employee.setCommissionPercentage(employee.getCommissionPercentage());
		employee.setEmployeeType(employee.getEmployeeType());
		employee.setFosType(employee.getFosType());
		employee.setPaymentType(employee.getPaymentType());
		employee.setHasIncentive(employee.getHasIncentive());
		employee.setIfscCode(employee.getIfscCode());
		employee.setTotalSales(employee.getTotalSales());
		employee.setIncentiveAmount(employee.getIncentiveAmount());
		employee.setEmail(employee.getEmail());
		employee.setPanCard(panFilePath);
		employee.setStatus(employee.getStatus());
		employeeRepository.save(employee);
		return employeeID;
	}

	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}

	public Employee findByEmployeeId(String id) {
		return employeeRepository.findByEmployeeId(id);
	}

	public void updatePassword(Employee employee, String newPassword) {
		employee.setPassword(passwordEncoder.encode(newPassword));
		employeeRepository.save(employee);
	}

	public Employee findByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}

	public List<Employee> getAllUsers() {

		return employeeRepository.findAll();
	}

	public Employee getById(Integer id) {
		return employeeRepository.getById(id);
	}

	public void updateEmployee(Employee employeeUpdate) {

		employeeRepository.save(employeeUpdate);

	}

	public void changepassword(Employee admin, String oldPassword, String newPassword, String confirmPassword) {
		if (passwordEncoder.matches(oldPassword, admin.getPassword())) {
			if (newPassword.equals(confirmPassword)) {
				admin.setPassword(passwordEncoder.encode(newPassword));
				updateEmployee(admin);
			} else {
				throw new RuntimeException("New Password and Old Password Doesn't Match");
			}
		} else {
			throw new RuntimeException("New Password and Old Password doesn't match");
		}
	}

	public void remove(String id) {
		Employee employee = findByEmployeeId(id);
		if (employee == null) {
			throw new RuntimeException("User Not Found");
		}
		if (employee.getRole().equals("ADMIN")) {

			throw new RuntimeException("Admin cannot be deleted");
		}
		
		if (employee.getPanCard() != null) {
			fileUploadService.deleteOldFile(employee.getPanCard());
		}
		if (employee.getAdhaarCard() != null) {
            fileUploadService.deleteOldFile(employee.getAdhaarCard());
		}
		employeeRepository.delete(employee);
	}

	public long countEmployee() {
		return employeeRepository.count();
	}

	public List<Employee> findbyOffice() {

		return employeeRepository.findByOffice();

	}

	public List<Employee> findbyFOS() {

		return employeeRepository.findByFOS();

	}

	public List<Employee> getAllUsersExceptAdmin() {
		return employeeRepository.findAllExcept();
	}

	public String registerNewUser(Employee employee) {
		String employeeID = generateReferralCode();
		employee.setEmployeeId(employeeID);
		employee.setPassword(passwordEncoder.encode(employee.getPassword()));
		employeeRepository.save(employee);
		return employeeID;
	}
}
