package com.recovery.managementsystem.controller;

import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.PasswordResetToken;
import com.recovery.managementsystem.service.EmployeeService;
import com.recovery.managementsystem.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final EmployeeService employeeService;
 

    public LoginController(EmployeeService employeeService) {	
        this.employeeService = employeeService;
    }
    
    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
       
        if (session.getAttribute("employee") != null) {
            return "redirect:/dashboard";
        }
        return "login/login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String employeeId,
            @RequestParam String password,RedirectAttributes redirectAttributes) 
    {
    	try {
    		      Employee employee= employeeService.validateUser(employeeId, password);  
    	            return "redirect:/dashboard"; 
    	}
		catch (RuntimeException e) 
		{
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			 System.out.println("Flash attribute set: " + e.getMessage()); 
            return "redirect:/login";
        }
    }
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "/admin/forgot-password";
    }
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        try {
            PasswordResetToken resetToken = passwordResetService.validateToken(token);
            model.addAttribute("token", resetToken.getToken());
            return "reset-password"; 
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/reset-password-error"; 
        }
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,RedirectAttributes redirectAttributes) {
    	
    	try {
    		Employee user = employeeService.findByEmail(email);
            if(user==null)
            {
           	 throw new RuntimeException("Email not found");
            }
            else{        	 
       passwordResetService.createPasswordResetToken(user);
       redirectAttributes.addFlashAttribute("successMessage","Email Sent Successfully");
       return "redirect:/login";
		}
    	}
    	catch (RuntimeException e) {
    		redirectAttributes.addFlashAttribute("error",e.getMessage());
    		return "redirect:/login";
			
		}
    	
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password,RedirectAttributes redirectAttributes){
        PasswordResetToken resetToken = passwordResetService.validateToken(token);
        Employee user = resetToken.getEmployee();
        employeeService.updatePassword(user, password);
        passwordResetService.deleteToken(resetToken);
        redirectAttributes.addFlashAttribute("successMessage", "Password Reset Successfully");
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}