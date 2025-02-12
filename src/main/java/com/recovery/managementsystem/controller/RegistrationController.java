/*
 * package com.recovery.managementsystem.controller;
 * 
 * import javax.validation.Valid;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.validation.BindingResult; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.ModelAttribute; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.servlet.mvc.support.RedirectAttributes;
 * 
 * import com.recovery.managementsystem.model.Employee; import
 * com.recovery.managementsystem.service.EmployeeService;
 * 
 * @Controller public class RegistrationController {
 * 
 * @Autowired EmployeeService employeeService;
 * 
 * public RegistrationController(EmployeeService employeeService) {
 * this.employeeService = employeeService; }
 * 
 * @GetMapping("/add-member") public String register(Model model) { Employee
 * employee=new Employee(); model.addAttribute("employee", employee); return
 * "add-member";
 * 
 * }
 * 
 * @PostMapping("/add-member") public String
 * registerEmployee(@Valid @ModelAttribute("employee") Employee
 * employee,BindingResult result,Model model,RedirectAttributes
 * redirectAttributes) {
 * 
 * if(result.hasErrors()) {
 * model.addAttribute("errorMessage","Registration Failed"); return "register";
 * } try { String pass=employee.getPassword(); String
 * id=employeeService.registerUser(employee);
 * redirectAttributes.addFlashAttribute("message", "Registration Successful");
 * redirectAttributes.addFlashAttribute("id", id);
 * redirectAttributes.addFlashAttribute("password", pass); return
 * "redirect:/success"; } catch (IllegalArgumentException e) {
 * model.addAttribute("error", e.getMessage()); return "register";
 * 
 * } }
 * 
 * @GetMapping("/success") public String showSuccessPage() { return "success"; }
 * }
 * 
 */