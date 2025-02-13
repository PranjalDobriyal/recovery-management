package com.recovery.managementsystem.controller;


import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.Expense;
import com.recovery.managementsystem.model.FundManage;
import com.recovery.managementsystem.model.FundSummary;
import com.recovery.managementsystem.security.CustomUserDetails;
import com.recovery.managementsystem.service.EmployeeService;
import com.recovery.managementsystem.service.FundService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/dashboard")
public class UserDashboardController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private FundService fundService;
	
	@GetMapping
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model,HttpSession session) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
	        return "redirect:/login";
	    }
    String id=userDetails.getUsername();
    Employee employee=employeeService.findByEmployeeId(id);
    BigDecimal totalCredit=fundService.getTotalCredit(employee.getEmployeeId());
    BigDecimal totalDebit=fundService.getTotalDebit(employee.getEmployeeId());
    session.setAttribute("fundBalance", employee.getWalletBalance());
    session.setAttribute("employeeId", id);
    session.setAttribute("employee", employee);
    session.setAttribute("totalCredit", totalCredit);
    session.setAttribute("totalDebit",totalDebit);
        return "user/dashboard";
    }
	@GetMapping("/user-profile")
	public String userProfile()
	{
		return "user/user-profile";
	}
	@GetMapping("/help")
	public String help()
	{
		return "user/404";
	}
	@GetMapping("/edit-profile")
	public String editProfile(@RequestParam Integer id,Model model) {
		Employee employee=employeeService.getById(id);
		model.addAttribute("employee", employee);
		return "user/edit-profile";
	}
	@GetMapping("/change-password")
	public String changePasswordView(HttpSession session) {
		return "user/change-password";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			@RequestParam String confirmPassword, HttpSession session, RedirectAttributes redirectAttributes) {
		try {

			Employee admin = (Employee) session.getAttribute("employee");
			employeeService.changepassword(admin, oldPassword, newPassword, confirmPassword);

			redirectAttributes.addFlashAttribute("success", "Password Changed Successfully");
			return "redirect:/dashboard/change-password";
		} catch (RuntimeException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/dashboard/change-password";

		}

	}
	@GetMapping("fund-details")
	public String fundDetails(Model model,HttpSession session) {
		 String id=(String) session.getAttribute("employeeId");
		 FundSummary fundSummary=fundService.getFundSummary(id);
		 model.addAttribute("id", id);
		 model.addAttribute("funds", fundSummary);
		return "user/fund-details";
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
		
		return "user/emp-fund";

	}

}
