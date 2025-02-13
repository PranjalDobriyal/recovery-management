package com.recovery.managementsystem.controller;

import com.recovery.managementsystem.model.*;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import com.recovery.managementsystem.model.Allowance.AllowanceType;
import com.recovery.managementsystem.security.CustomUserDetails;
import com.recovery.managementsystem.service.*;

import org.apache.logging.log4j.core.util.PasswordDecryptor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	private EmployeeService employeeService;
	private AllowanceService allowanceService;
	private EmployeeAllowanceService employeeAllowanceService;
	private ExpenseService expenseService;
	private ClientService clientService;
	private RecoveryService recoveryService;
	private FileUploadService fileUploadService;
	private FundService fundService;
	private PasswordEncoder PasswordEncoder;


	public AdminController(EmployeeService employeeService, AllowanceService allowanceService,
			EmployeeAllowanceService employeeAllowanceService, ExpenseService expenseService,
			ClientService clientService, RecoveryService recoveryService, FileUploadService fileUploadService,
			FundService fundService) {
		this.employeeService = employeeService;
		this.allowanceService = allowanceService;
		this.employeeAllowanceService = employeeAllowanceService;
		this.expenseService = expenseService;
		this.clientService = clientService;
		this.recoveryService = recoveryService;
		this.fileUploadService = fileUploadService;
		this.fundService = fundService;

	}

	@GetMapping("/dashboard")
	public String showAdminDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model,
			HttpSession session) {
		String id = userDetails.getUsername();
		Employee employee = employeeService.findByEmployeeId(id);
		Long countEmployee = employeeService.countEmployee() - 1;
		List<Employee> employees = employeeService.getAllUsers();
		BigDecimal expenseTotal = expenseService.getTotal();
		Integer officeCount = employeeService.findbyOffice().size();
		Integer fosCount = employeeService.findbyFOS().size();
		BigDecimal totalFund=fundService.getTotalFund();
		model.addAttribute("employees", employees);
		session.setAttribute("countEmployee", countEmployee);
		session.setAttribute("id", id);
		session.setAttribute("totalexpense", expenseTotal);
		session.setAttribute("admin", employee);
		session.setAttribute("officeCount", officeCount);
		session.setAttribute("fosCount", fosCount);
		session.setAttribute("totalFund", totalFund);
		return "admin/admin-dashboard";
	}

	@GetMapping("/users")
	public String getAllUsers(Model model, HttpSession session) {
		List<Employee> employees = employeeService.getAllUsers();
		model.addAttribute("employees", employees);
		session.setAttribute("employees", employees);
		return "admin/employees";
	}

	@GetMapping("/edit/{id}")
	public String editUser(@PathVariable Integer id, Model model) {
		Employee employee = employeeService.getById(id);
	
		model.addAttribute(employee);
		model.addAttribute("employeeTypes", employee.getEmployeeType().values());
		model.addAttribute("fosTypes", employee.getFosType().values());
		model.addAttribute("paymentTypes", employee.getPaymentType().values());
		return "admin/editEmployeeDetails";
	}

	@PostMapping("/edit/{id}")
	public String updateUserProfile(@PathVariable("id") Integer id, @ModelAttribute Employee employee,
			@RequestParam(required = false) MultipartFile panCardFile,
			@RequestParam(required = false) MultipartFile adhaarCardFile, RedirectAttributes redirectAttributes) {

		Employee employeeUpdate = employeeService.getById(id);
		if (employeeUpdate == null) {
			redirectAttributes.addFlashAttribute("error", "Employee Not Found");
			return "redirect:/edit/{id}";
		}
		if (employeeUpdate.getRole().equals("ADMIN")) {
			redirectAttributes.addFlashAttribute("error", "Admin Hun Main Ladle");
			return "redirect:/admin/users";
		}
		try {
			if(!panCardFile.isEmpty() && !adhaarCardFile.isEmpty()) {
			String panFilePath = fileUploadService.saveDocument(panCardFile, employeeUpdate.getPanCard());
			String aadhaarFilePath = fileUploadService.saveDocument(adhaarCardFile, employeeUpdate.getAdhaarCard());
			employeeUpdate.setAdhaarCard(aadhaarFilePath);
			employeeUpdate.setPanCard(panFilePath);
			}
			if(!panCardFile.isEmpty()) {
				String panFilePath = fileUploadService.saveDocument(panCardFile, employeeUpdate.getPanCard());
				employeeUpdate.setPanCard(panFilePath);
				}
			if(!adhaarCardFile.isEmpty()) {
				
				String aadhaarFilePath = fileUploadService.saveDocument(adhaarCardFile,	employeeUpdate.getAdhaarCard());
				employeeUpdate.setAdhaarCard(aadhaarFilePath);
				
				}
			employeeUpdate.setEmployeeName(employee.getEmployeeName());
			employeeUpdate.setAccountNumber(employee.getAccountNumber());
			employeeUpdate.setAddress(employee.getAddress());
			employeeUpdate.setSalary(employee.getSalary());
			employeeUpdate.setBankName(employee.getBankName());
			employeeUpdate.setCommissionPercentage(employee.getCommissionPercentage());
			employeeUpdate.setEmployeeType(employee.getEmployeeType());
			employeeUpdate.setFosType(employee.getFosType());
			employeeUpdate.setPaymentType(employee.getPaymentType());
			employeeUpdate.setHasIncentive(employee.getHasIncentive());
			employeeUpdate.setIfscCode(employee.getIfscCode());
			employeeUpdate.setTotalSales(employee.getTotalSales());
			employeeUpdate.setIncentiveAmount(employee.getIncentiveAmount());
			employeeUpdate.setEmail(employee.getEmail());
			
			employeeUpdate.setStatus(employee.getStatus());
			employeeService.updateEmployee(employeeUpdate);

			redirectAttributes.addFlashAttribute("success", "Updated Successfully");

			return "redirect:/admin/users";
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error",
					"File upload failed! Please select both PAN and Aadhaar files.");
			return "redirect:/admin/edit/{id}";
		}
		
	}

	@GetMapping("/change-password")
	public String changePasswordView(HttpSession session) {
		return "admin/change-password";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			@RequestParam String confirmPassword, HttpSession session, RedirectAttributes redirectAttributes) {
		try {

			Employee admin = (Employee) session.getAttribute("admin");
			employeeService.changepassword(admin, oldPassword, newPassword, confirmPassword);

			redirectAttributes.addFlashAttribute("success", "Password Changed Successfully");
			return "redirect:/admin/change-password";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/change-password";

		}

	}

	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
		try {
			employeeService.remove(id);
			redirectAttributes.addFlashAttribute("success", "Employee Removed Successfully");
			return "redirect:/admin/users";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/users";

		}
	}

	@GetMapping("/add-member")
	public String register(Model model, HttpSession session) {
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		model.addAttribute("employeeTypes", employee.getEmployeeType().values());

		model.addAttribute("fosTypes", employee.getFosType().values());
		model.addAttribute("paymentTypes", employee.getPaymentType().values());

		session.setAttribute("employeeTypes", employee.getEmployeeType().values());
		session.setAttribute("fosTypes", employee.getFosType().values());
		session.setAttribute("paymentTypes", employee.getPaymentType().values());
		return "admin/add-member";

	}

	@PostMapping("/add-member")
	public String register(@Valid @ModelAttribute("employee") Employee employee, BindingResult result,
			@RequestParam("panCardFile") MultipartFile panCardFile,
			@RequestParam("adhaarCardFile") MultipartFile adhaarCardFile, Model model,
			RedirectAttributes redirectAttributes) throws IOException {

		if (result.hasErrors()) {
			model.addAttribute("errorMessage", "Registration Failed");
			logger.info("Registration failed  : {}");
			return "admin/add-member";
		}
		try {
			if (panCardFile.isEmpty() || adhaarCardFile.isEmpty()) {
				model.addAttribute("errorMessage", "File upload failed! Please select both PAN and Aadhaar files.");
				return "admin/add-member";
			}
			String panFilePath = fileUploadService.saveDocument(panCardFile,null);
			String aadhaarFilePath = fileUploadService.saveDocument(adhaarCardFile,null);
			String pass = employee.getPassword();
			String id = employeeService.registerUser(employee, panFilePath, aadhaarFilePath);
			redirectAttributes.addFlashAttribute("message", "Registration Successful");
			redirectAttributes.addFlashAttribute("id", id);
			redirectAttributes.addFlashAttribute("password", pass);
			return "redirect:/admin/success";
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			return "admin/users";

		}
	}

	@GetMapping("/{id}/documents/{type}")
	public String viewEmployeeDocuments(@PathVariable String id, @PathVariable String type, Model model) {
		Employee employee = employeeService.findByEmployeeId(id);
		if (employee == null) {
			model.addAttribute("errorMessage", "Employee not found!");
			return "error";
		}

		String fileName = "";
		if ("pan".equalsIgnoreCase(type)) {
			fileName = employee.getPanCard();
		} else if ("adhaar".equalsIgnoreCase(type)) {
			fileName = employee.getAdhaarCard();
		} else {
			model.addAttribute("errorMessage", "Invalid document type!");
			return "error";
		}

		model.addAttribute("employee", employee);
		model.addAttribute("fileName", fileName);
		return "admin/view-documents"; // Thymeleaf template for viewing documents
	}

	@GetMapping("/help")
	public String showhelpPage() {
		return "admin/404";
	}

	@GetMapping("/success")
	public String showSuccessPage() {
		return "admin/success";
	}

	@GetMapping("/allowances")
	public String allowances(Model model, HttpSession session) {
		List<Allowance> allowances = allowanceService.getAll();
		Allowance allowance = new Allowance();
		model.addAttribute("allowances", allowances);
		session.setAttribute("allowances", allowances);
		session.setAttribute("allowanceTypes", allowance.getAllowanceType().values());
		return "admin/allowances";
	}

	@GetMapping("/add-allowance")
	public String addAllowance(Model model, HttpSession session) {
		Allowance allowance = new Allowance();
		model.addAttribute("allowance", allowance);
		return "admin/add-allowance";
	}

	@PostMapping("/add-allowance")
	public String addAllowance(@ModelAttribute("allowance") Allowance allowance, Model model,
			RedirectAttributes redirectAttributes) {

		try {
			allowanceService.addAllowance(allowance);
			redirectAttributes.addFlashAttribute("success", "Allowance added successfully");
			return "redirect:/admin/allowances";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/add-allowance";

		}
	}

	@PostMapping("/edit-allowance/{id}")
	public String updateAllowance(@PathVariable("id") Integer id, @ModelAttribute Allowance allowance,
			RedirectAttributes redirectAttributes) {

		Allowance allowanceUpdate = allowanceService.getById(id);
		if (allowanceUpdate == null) {
			redirectAttributes.addFlashAttribute("error", "Allowance Not Found");
			return "redirect:/admin/allowances";
		}
		allowanceUpdate.setStatus(allowance.getStatus());
		allowanceUpdate.setAllowanceName(allowance.getAllowanceName());
		allowanceUpdate.setAllowanceShort(allowance.getAllowanceShort());
		allowanceUpdate.setAllowanceType(allowance.getAllowanceType());
		allowanceUpdate.setStatus(allowance.getStatus());
		allowanceService.updateAllowance(allowanceUpdate);
		redirectAttributes.addFlashAttribute("success", "Allowance updated ");

		return "redirect:/admin/allowances";
	}

	@GetMapping("/edit-allowance/{id}")
	public String editAllowance(@PathVariable Integer id, Model model) {
		Allowance allowance = allowanceService.getById(id);
		model.addAttribute(allowance);
		return "admin/edit-allowance";
	}

	@GetMapping("/add-emp-allowances/{id}")
	public String addEmpAllow(@PathVariable("id") String id, Model model) {
		// Fetch employee details
		Employee employee = employeeService.findByEmployeeId(id);

		// Fetch all available allowances
		List<Allowance> allowances = allowanceService.getAll();
		
		// Extract existing assigned allowances (only IDs)
		Set<Integer> employeeAllowanceIds = employee.getEmployeeAllowances().stream()
				.map(empAllow -> empAllow.getAllowance().getAllowanceId()) // Extract allowance ID
				.collect(Collectors.toSet());
		Map<Integer, BigDecimal> amounts=new HashMap<>();
		Map<Integer, Double> percents=new HashMap<>();
		
		for (Integer allowId : employeeAllowanceIds) {
			BigDecimal Amount=employeeAllowanceService.findAmount(id,allowId);
			Double Percent=employeeAllowanceService.findPercent(id,allowId);
			if(Amount==null)
			{
				percents.put(allowId, Percent);
			}
			if(Percent==null)
			{
				amounts.put(allowId, Amount);
			}
			
		}
		

		// Add attributes to model
		model.addAttribute("amounts",amounts);
		model.addAttribute("percents", percents);
		model.addAttribute("employee", employee);
		model.addAttribute("allowanceTypes",AllowanceType.values());
		model.addAttribute("employeeAllowanceIds", employeeAllowanceIds); 
		model.addAttribute("allowances", allowances);

		return "admin/add-emp-allowances"; // Thymeleaf template
	}

	@PostMapping("/add-emp-allowances/{id}")
	public String addEmpAllow(@PathVariable("id") String id, @RequestParam(required = false) List<Integer> allowanceIds,
			@RequestParam Map<String, String> allRequestParams, RedirectAttributes redirectAttributes) {

		logger.info("Starting addEmpAllow method for employee ID: {}", id);

		try {
			// Fetch the employee by ID
			Employee employee = employeeService.findByEmployeeId(id);
			if (employee == null) {
				logger.error("Employee not found with ID: {}", id);
				throw new RuntimeException("Employee Not Found");
			}

			// Fetch allowances for the provided IDs
			Set<Allowance> newAllowances = (allowanceIds != null)
					? new HashSet<>(allowanceService.findAllById(allowanceIds))
					: new HashSet<>();

			// Create a lookup map for quick access to each Allowance by its ID.
			Map<Integer, Allowance> allowanceLookup = newAllowances.stream()
					.collect(Collectors.toMap(Allowance::getAllowanceId, Function.identity()));

			Map<Integer, BigDecimal> allowanceAmounts = new HashMap<>();
			Map<Integer, Double> allowancePercentages = new HashMap<>();

			if (allowanceIds != null) {
				for (Integer allowanceId : allowanceIds) {
					// Construct the expected parameter name (e.g., "allowanceValue_101")
					String paramName = "allowanceValue_" + allowanceId;
					if (allRequestParams.containsKey(paramName)) {
						String valueStr = allRequestParams.get(paramName);

						// Retrieve the Allowance using the lookup map to check its type (enum)
						Allowance allowance = allowanceLookup.get(allowanceId);
						if (allowance != null) {
							if (allowance.getAllowanceType() == AllowanceType.AMOUNT) {
								try {
									BigDecimal amountValue = new BigDecimal(valueStr);
									allowanceAmounts.put(allowanceId, amountValue);
								} catch (NumberFormatException ex) {
									logger.error("Invalid amount value for allowance ID {}: {}", allowanceId, valueStr);
									// Handle error appropriately (e.g., set default, skip, or return an error
									// response)
								}
							} else if (allowance.getAllowanceType() == AllowanceType.PERCENTAGE) {
								try {
									Double percentageValue = Double.valueOf(valueStr);
									allowancePercentages.put(allowanceId, percentageValue);
								} catch (NumberFormatException ex) {
									logger.error("Invalid percentage value for allowance ID {}: {}", allowanceId,
											valueStr);
									// Handle error appropriately
								}
							}
						}
					}
				}
			}
			// Pass the new allowances and their respective values to your service layer.
			// Ensure your service method signature is updated to accept these maps.
			employeeAllowanceService.updateAllowances(newAllowances, allowanceAmounts, allowancePercentages, employee);

			logger.info("Allowances updated successfully for employee ID: {}", id);
			redirectAttributes.addFlashAttribute("success", "Allowances updated successfully!");
			return "redirect:/admin/users";

		} catch (RuntimeException e) {
			logger.error("Error occurred while updating allowances for employee ID: {}", id, e);
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/users";
		}
	}

	@GetMapping("/expense")
	public String listExpenses(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size,
			@RequestParam(required = false) String category, @RequestParam(required = false) String name,
			@RequestParam(required = false) String paymentType,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
			Model model) {
		 Expense.PaymentType selectedPaymentType = null;
		    if (paymentType != null && !paymentType.isEmpty()) {
		        try {
		        	selectedPaymentType = Expense.PaymentType.valueOf(paymentType);
		        } catch (IllegalArgumentException e) {
		        	selectedPaymentType = null;
		        }
		    }

		List<ExpenseCategory> categoryList = expenseService.getAll();
		Set<String> names = expenseService.findAllByName();
		

		Page<Expense> expensePage = expenseService.getFilteredExpenses(page, size, category, name, fromDate, toDate,
				paymentType);
		BigDecimal totalAmount = expenseService.getFilteredTotalAmount(category, name, fromDate, toDate, paymentType);

		model.addAttribute("categoryList", categoryList);
		model.addAttribute("expenses", expensePage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("paymentTypes", Expense.PaymentType.values());
		model.addAttribute("totalPages", expensePage.getTotalPages());
		model.addAttribute("names", names);
		model.addAttribute("size", size);
		model.addAttribute("selectedCategory", category);
		model.addAttribute("selectedName", name);
		model.addAttribute("selectedFromDate", fromDate);
		model.addAttribute("selectedToDate", toDate);
		model.addAttribute("selectedPaymentType", selectedPaymentType);
		model.addAttribute("totalAmount", totalAmount);

		return "admin/expense";
	}

	@GetMapping("/add-expense")
	public String addExpense(Model model) {
		Expense expense = new Expense();
		List<ExpenseCategory> categoryList = expenseService.getAll();
		model.addAttribute("expense", expense);
		model.addAttribute("paymentTypes", expense.getPaymentType().values());
		model.addAttribute("categoryList", categoryList);
		return "admin/add-expense";

	}

	@PostMapping("/add-expense")
	public String addExpense(@ModelAttribute Expense expense, RedirectAttributes redirectAttributes) {
		expenseService.save(expense);
		redirectAttributes.addFlashAttribute("success", "Expense Added ");
		return "redirect:/admin/expense";
	}

	@GetMapping("/category")
	public String getCategory(Model model) {
		List<ExpenseCategory> category = expenseService.getAll();
		model.addAttribute("category", category);
		return "admin/category";

	}

	@GetMapping("/add-category")
	public String addCategory(Model model) {
		ExpenseCategory excategory = new ExpenseCategory();

		model.addAttribute("excategory", excategory);
		return "admin/add-category";

	}

	@PostMapping("/edit-category/{id}")
	public String editCategory(@PathVariable("id") Integer id, @ModelAttribute ExpenseCategory exCategory,
			RedirectAttributes redirectAttributes) {

		ExpenseCategory updatedcategory = expenseService.getById(id);
		if (updatedcategory == null) {
			redirectAttributes.addFlashAttribute("error", "Category Not Found");
			return "redirect:/admin/category";
		}

		expenseService.updateCategory(updatedcategory, exCategory);
		redirectAttributes.addFlashAttribute("success", "Category updated ");

		return "redirect:/admin/category";
	}

	@GetMapping("/edit-category/{id}")
	public String editCategory(@PathVariable Integer id, Model model) {
		ExpenseCategory excategory = expenseService.getById(id);
		model.addAttribute("category", excategory);
		return "admin/edit-category";
	}

	@PostMapping("/add-category")
	public String addCategory(@Valid @ModelAttribute ExpenseCategory excategory, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		try {
			expenseService.savecategory(excategory);
			redirectAttributes.addFlashAttribute("success", "Category Added ");
			return "redirect:/admin/category";

		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/category";
		}

	}

	@DeleteMapping("/delete-category/{id}")
	public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		expenseService.deleteCategoryById(id);
		redirectAttributes.addFlashAttribute("success", "Category Deleted");
		return "redirect:/admin/category";
	}

	@DeleteMapping("/delete-expense/{id}")
	public String deleteExpense(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		expenseService.deleteById(id);
		redirectAttributes.addFlashAttribute("success", "Expense Deleted");
		return "redirect:/admin/expense";
	}

	@DeleteMapping("/delete-allowance/{id}")
	public String deleteAllowance(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		allowanceService.deleteById(id);
		redirectAttributes.addFlashAttribute("success", "Allowance Deleted");
		return "redirect:/admin/allowances";
	}

	@GetMapping("/client")
	public String getClient(Model model) {
		List<Client> clients = clientService.findAll();
		model.addAttribute("clients", clients);
		return "admin/clients";
	}

	@GetMapping("/add-client")
	public String addClient(Model model) {
		Client client = new Client();
		model.addAttribute("client", client);
		return "admin/add-clients";
	}

	@PostMapping("/add-client")
	public String addClient(@ModelAttribute Client client, RedirectAttributes redirectAttributes) {
		clientService.saveclient(client);
		redirectAttributes.addFlashAttribute("success", "Category Added ");
		return "redirect:/admin/client";
	}

	@GetMapping("/recovery")
	public String recovery(Model model) {
		List<Recovery> recoveries = recoveryService.findAll();
		model.addAttribute("recovery", recoveries);
		return "admin/recovery";
	}

	@GetMapping("/alot/recovery/{id}")
	public String alot(@PathVariable("id") Integer id, Model model) {
		Recovery recovery = recoveryService.findById(id);
		List<Employee> fosEmployees = employeeService.findbyFOS();
		model.addAttribute("recovery", recovery);
		model.addAttribute("fos", fosEmployees);
		return "admin/alot-list";
	}

	@PostMapping("/alot/recovery/{id}")
	public String alot(@PathVariable("id") Integer id, @RequestParam String employeeId,
			RedirectAttributes redirectAttributes) {
		Recovery recovery = recoveryService.findById(id);
		Employee employee = employeeService.findByEmployeeId(employeeId);
		try {
			recovery.setEmployee(employee);
			recoveryService.save(recovery);
			redirectAttributes.addFlashAttribute("success", "Alloted");
			return "redirect:/admin/recovery";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Cannot Be Alloted");

			return "redirect:admin/recovery";

		}

	}

	@GetMapping("/add-csv")
	public String addcsv() {

		return "admin/uploadcsv";
	}

	@PostMapping("/add-csv")
	public String addcsv(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			recoveryService.importCsv(file);
			redirectAttributes.addFlashAttribute("success", "CSV file uploaded and processed successfully.");
			return "redirect:/admin/recovery";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/recovery";

		}

	}

	

}