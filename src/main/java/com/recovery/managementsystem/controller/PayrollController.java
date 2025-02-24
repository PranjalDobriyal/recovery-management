package com.recovery.managementsystem.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.recovery.managementsystem.model.Deductions;
import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.Payroll;
import com.recovery.managementsystem.model.PayrollSummary;
import com.recovery.managementsystem.service.EmployeeService;
import com.recovery.managementsystem.service.PayrollService;

@Controller
@RequestMapping("/admin")
public class PayrollController {
	
	private EmployeeService employeeService;
	private PayrollService payrollService;
	
	
	public PayrollController(EmployeeService employeeService, PayrollService payrollService) {
		this.employeeService = employeeService;
		this.payrollService = payrollService;
	}
	@GetMapping("/payroll")
	public String payroll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size,
			@RequestParam(required = false) Integer year, @RequestParam(required = false) String month,
			Model model)
	{
		if (year == null) {
	        year = LocalDate.now().getYear();
	    }
		
		 Month enumMonth;
		    if ("all".equalsIgnoreCase(month)) {
		        // If "all" is selected, set enumMonth to null
		        enumMonth = null;
		    } else if (month == null) {
		        // If no month is provided, default to the current month
		        enumMonth = LocalDate.now().getMonth();
		    } else {
		        // If a specific month is provided, convert it to a Month enum
		        try {
		            enumMonth = Month.valueOf(month.toUpperCase());
		        } catch (IllegalArgumentException e) {
		            // Handle invalid month name
		            model.addAttribute("error", "Invalid month name provided.");
		            enumMonth = null; // Set to null to avoid further issues
		        }
		    }
	
		
		Page<PayrollSummary> payrolls=payrollService.getPayrollSummary(page,size,year,enumMonth);
		BigDecimal totalAllowance=payrollService.getTotalAllowance(enumMonth,year);
		BigDecimal totalDeduction=payrollService.getTotalDeduction(enumMonth,year);
		
		model.addAttribute("payrolls",payrolls.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", payrolls.getTotalPages());
		model.addAttribute("totalAllowances", totalAllowance);
		model.addAttribute("totalDeductions", totalDeduction);

		model.addAttribute("size", size);
		model.addAttribute("selectedMonth", month);
		model.addAttribute("selectedYear", year);

		return "admin/payrolls";
	}
	@GetMapping("/payroll/generate")
	public String addPayroll(@RequestParam int year, @RequestParam String month,
	                         RedirectAttributes redirectAttributes, Model model) {
	    try {
	        Month monthEnum = Month.valueOf(month.toUpperCase()); // Convert String to Enum
	        payrollService.generatePayroll(year, monthEnum);
	        redirectAttributes.addFlashAttribute("success", "Payroll generated successfully");
	        model.addAttribute("selectedMonth", monthEnum);
	        model.addAttribute("selectedYear", year);
	        return "redirect:/admin/payroll";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/admin/payroll";
	    }
	}

	@GetMapping("/payroll/lock")
	public String lockPayroll(@RequestParam Integer year, @RequestParam String month,
	                          RedirectAttributes redirectAttributes, Model model) {
	    try {
	        Month monthEnum = Month.valueOf(month.toUpperCase()); // Convert String to Enum
	        payrollService.lockPayroll(monthEnum, year);
	        redirectAttributes.addFlashAttribute("success", "Payroll Locked");
	        return "redirect:/admin/payroll";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/admin/payroll";
	    }
	}

	@GetMapping("/add-deduction")
	public String addDedution(@RequestParam String id,@RequestParam Integer year,@RequestParam Month month,Model model)
	{
		model.addAttribute("id", id);
		
	Deductions deduction=payrollService.findDeductionById(id,month,year);
	model.addAttribute("deduction", deduction);
	model.addAttribute("year", year);
	model.addAttribute("month", month);
	
		return "admin/add-deduction";
	}
	
	@PostMapping("/add-deduction")
	public String addDeduction(@RequestParam String employeeId,@RequestParam Integer year,@RequestParam Month month, @RequestParam BigDecimal amount,@RequestParam String name,RedirectAttributes redirectAttributes)
	{
		try {
			payrollService.addDeduction(name,employeeId,amount,month,year);
			redirectAttributes.addFlashAttribute("success", "Deduction Added");
			return "redirect:/admin/payroll";
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/payroll";
		}
		

	}
	@GetMapping("/generate/payslip")
	public String generatePayslip(@RequestParam String id,Model model,@RequestParam Month month,@RequestParam Integer year)
	{
		List<Payroll> payrolls=payrollService.getAllPayrollsByEmployeeId(id,month,year);
		Deductions deduction=payrollService.getAllDeductionByEmployeeId(id,month,year);
		Employee employee=employeeService.findByEmployeeId(id);
		PayrollSummary payrollSummary=payrollService.getPayrollSummaryByEmployeeId(id, month, year);
		model.addAttribute("employee", employee);
		model.addAttribute("payrolls", payrolls);
		model.addAttribute("month", month);
		model.addAttribute("year",year);
		model.addAttribute("year",year);
		model.addAttribute("summary",payrollSummary);
		model.addAttribute("deduction", deduction);
		return "admin/pay-slip";
		
	}

}	

