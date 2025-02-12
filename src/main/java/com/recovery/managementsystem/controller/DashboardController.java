package com.recovery.managementsystem.controller;


import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.security.CustomUserDetails;
import com.recovery.managementsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model,HttpSession session) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
	        return "redirect:/login";
	    }
    String id=userDetails.getUsername();
    Employee employee=employeeService.findByEmployeeId(id);
    session.setAttribute("employeeId", id);
    session.setAttribute("employee", employee);
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

}
