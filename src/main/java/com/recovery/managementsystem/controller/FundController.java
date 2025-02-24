package com.recovery.managementsystem.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.Expense;
import com.recovery.managementsystem.model.FundManage;
import com.recovery.managementsystem.model.FundSummary;
import com.recovery.managementsystem.service.EmployeeService;
import com.recovery.managementsystem.service.FundService;

@Controller
@RequestMapping("/admin")
public class FundController {

	private FundService fundService;
	private EmployeeService employeeService;

	public FundController(FundService fundService, EmployeeService employeeService) {
		this.fundService = fundService;
		this.employeeService = employeeService;
	}

	@GetMapping("/funds")
	public String getAllFunds(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size,
			@RequestParam(required = false) String employeeId,
			@RequestParam(required = false) String amountType,
			@RequestParam(required = false) String paymentMode, 
			@RequestParam(required = false) String category,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,

			Model model) {
		 Expense.PaymentType selectedPaymentMode = null;
		    if (paymentMode != null && !paymentMode.isEmpty()) {
		        try {
		            selectedPaymentMode = Expense.PaymentType.valueOf(paymentMode);
		        } catch (IllegalArgumentException e) {
		            selectedPaymentMode = null;
		        }
		    }
		amountType = (amountType != null && amountType.isEmpty()) ? null : amountType;
	    paymentMode = (paymentMode != null && paymentMode.isEmpty()) ? null : paymentMode;
	    category = (category != null && category.isEmpty()) ? null : category;
		BigDecimal totalFunds = fundService.getTotalFunds(employeeId,fromDate,toDate);
		List<Employee> allEmployees = employeeService.getAllUsersExceptAdmin();

		Page<FundSummary> fundPage = fundService.getFilteredExpenses(page, size, employeeId);

		model.addAttribute("funds", fundPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("allEmployees", allEmployees);
		model.addAttribute("totalFunds", totalFunds);
		/* model.addAttribute("selectedEmployee", employeeId); */
		model.addAttribute("selectedamountType", amountType);
	    model.addAttribute("selectedpaymentMode", selectedPaymentMode);
	    model.addAttribute("selectedcategory", category);
	    model.addAttribute("selectedfromDate", fromDate);
		model.addAttribute("selectedtoDate", toDate);
	    model.addAttribute("employee", employeeService.findByEmployeeId(employeeId));
		model.addAttribute("paymentTypes", com.recovery.managementsystem.model.Expense.PaymentType.values());

		/* model.addAttribute("totalAmount", totalAmount); */
		return "admin/funds";

	}

	@GetMapping("/add-fund")
	public String addFund(Model model) {
		FundManage fund = new FundManage();
		List<Employee> allEmployees = employeeService.getAllUsers();
		model.addAttribute("paymentTypes", com.recovery.managementsystem.model.Expense.PaymentType.values());
		model.addAttribute("fund", fund);
		model.addAttribute("allEmployees", allEmployees);
		return "admin/addfund";

	}

	@PostMapping("/add-fund")
	public String addFund(@ModelAttribute FundManage fund, HttpSession session, @RequestParam String employeeId,
			RedirectAttributes redirectAttributes, Model model) {
		try {
			Employee employee = employeeService.findByEmployeeId(employeeId);
			String approvedBy = (String) session.getAttribute("id");
			fundService.addFund(employee, fund, approvedBy);
			redirectAttributes.addFlashAttribute("success", "Fund Added !");
			return "redirect:/admin/funds";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/funds";
		}

	}

	@GetMapping("/emp-fund")
	public String getEmployeeFunds(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int size, 
			@RequestParam(name = "id") String id,
			@RequestParam(required = false) String amountType,
			@RequestParam(required = false) String paymentMode, 
			@RequestParam(required = false) String category,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
			Model model) {
		   Expense.PaymentType selectedPaymentMode = null;
		    if (paymentMode != null && !paymentMode.isEmpty()) {
		        try {
		            selectedPaymentMode = Expense.PaymentType.valueOf(paymentMode);
		        } catch (IllegalArgumentException e) {
		            selectedPaymentMode = null;
		        }
		    }
		amountType = (amountType != null && amountType.isEmpty()) ? null : amountType;
	    paymentMode = (paymentMode != null && paymentMode.isEmpty()) ? null : paymentMode;
	    category = (category != null && category.isEmpty()) ? null : category;
		Page<FundManage> fundPage = fundService.getFilteredFunds(page, size,id, amountType, paymentMode, category,fromDate,toDate);
		model.addAttribute("paymentTypes", com.recovery.managementsystem.model.Expense.PaymentType.values());
		model.addAttribute("funds", fundPage.getContent());
		model.addAttribute("currentPage", page);
	    model.addAttribute("size", size);
	    model.addAttribute("id", id);
	    model.addAttribute("selectedamountType", amountType);
	    model.addAttribute("selectedpaymentMode", selectedPaymentMode);
	    model.addAttribute("selectedcategory", category);
	    model.addAttribute("selectedfromDate", fromDate);
		model.addAttribute("selectedtoDate", toDate);
	    model.addAttribute("employee", employeeService.findByEmployeeId(id));
		model.addAttribute("paymentTypes", com.recovery.managementsystem.model.Expense.PaymentType.values());
		
		return "admin/emp-fund";

	}
}
