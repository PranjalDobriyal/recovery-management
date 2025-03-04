package com.recovery.managementsystem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.recovery.managementsystem.model.Deductions;
import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.EmployeeAllowance;
import com.recovery.managementsystem.model.Payroll;
import com.recovery.managementsystem.model.PayrollSummary;
import com.recovery.managementsystem.repository.DeductionsRepository;
import com.recovery.managementsystem.repository.PayrollRepository;
import com.recovery.managementsystem.repository.PayrollSummaryRepository;

@Service
@Transactional
public class PayrollService {
	private PayrollRepository payrollRepository;
	private EmployeeAllowanceService employeeAllowanceService;
	private EmployeeService employeeService;
	private DeductionsRepository deductionsRepository;
	private PayrollSummaryRepository payrollSummaryRepository;
	public PayrollService(PayrollRepository payrollRepository, EmployeeAllowanceService employeeAllowanceService,
			EmployeeService employeeService, DeductionsRepository deductionsRepository,
			PayrollSummaryRepository payrollSummaryRepository) {
		this.payrollRepository = payrollRepository;
		this.employeeAllowanceService = employeeAllowanceService;
		this.employeeService = employeeService;
		this.deductionsRepository = deductionsRepository;
		this.payrollSummaryRepository = payrollSummaryRepository;
	}

	public List<Payroll> getAllPayrolls() {
		return payrollRepository.findAll();
	}

	public void generatePayroll(int year, Month month) {
	    // Check if any payroll entry for the given month and year is locked
	    boolean isLocked = payrollRepository.existsByYearAndMonthAndStatus(year, month, "LOCKED");

	    if (isLocked) {
	        throw new RuntimeException("Payroll for " + month + " " + year + " is locked and cannot be regenerated.");
	    }

	    // Delete payroll only if it's not locked
	    payrollRepository.deleteByMonthAndYear(year, month);

	    // Generate payroll for all employees
	    generatePayrollForAllEmployees(month, year);
	}


	
	
	public void generatePayrollForAllEmployees(Month month, int year) {
	    List<Employee> employees = employeeService.getAllUsersExceptAdmin();
	    
	    for (Employee employee : employees) { 
	        List<EmployeeAllowance> employeeAllowances = employeeAllowanceService.getEmployeeAllowancesByEmployee_Id(employee.getEmployeeId());
	        
	        for (EmployeeAllowance ea : employeeAllowances) {
	            Payroll payroll = new Payroll();
	            payroll.setAllowanceAmount(ea.getAmount());
	            payroll.setEmployee(employee);
	            payroll.setAllowanceName(ea.getAllowance().getAllowanceName());
	            payroll.setAllowanceShort(ea.getAllowance().getAllowanceShort());
	            payroll.setMonth(month);  // Store the month
	            payroll.setYear(year);    // Store the year
	            payrollRepository.save(payroll);
	        }
	    }
	}

	
	public void generatePayrollSummary() {
	    Integer year = LocalDate.now().getYear();
	    List<Employee> employees = employeeService.getAllUsersExceptAdmin();

	    // Loop through all months from January to the current month
	    for (int monthValue = 1; monthValue <= 12; monthValue++) {
	        Month month = Month.of(monthValue); // Convert int to Month enum

	        for (Employee employee : employees) {
	            BigDecimal totalAllowance = BigDecimal.ZERO;
	            BigDecimal totalDeduction = BigDecimal.ZERO;

	            // Fetch payroll records for this employee for this month
	            List<Payroll> payrolls = payrollRepository.getPayrollByEmployeeId(employee.getEmployeeId(), month, year);
	            for (Payroll payroll : payrolls) {
	                if (payroll.getAllowanceAmount() != null) {
	                    totalAllowance = totalAllowance.add(payroll.getAllowanceAmount());
	                }
	            }

	            // Fetch deductions for this employee for this month
	            Deductions deduction = deductionsRepository.findAllByIdAndMonth(employee.getEmployeeId(), month, year);
	            if (deduction != null && deduction.getDeductionAmount() != null) {
	                totalDeduction = totalDeduction.add(deduction.getDeductionAmount());
	            }

	            // Check if payroll summary already exists
	            PayrollSummary payrollSummary = payrollSummaryRepository.findByEmployeeId(employee.getEmployeeId(), month, year);
	            if (payrollSummary != null) {
	                // Update existing payroll summary
	                payrollSummary.setTotalAllowance(totalAllowance);
	                payrollSummary.setTotalDeduction(totalDeduction);
	                payrollSummary.setNetSalary(totalAllowance.subtract(totalDeduction));
	            } else {
	                // Create new payroll summary
	                payrollSummary = new PayrollSummary();
	                payrollSummary.setEmployeeId(employee.getEmployeeId());
	                payrollSummary.setEmployeeName(employee.getEmployeeName());
	                payrollSummary.setTotalAllowance(totalAllowance);
	                payrollSummary.setTotalDeduction(totalDeduction);
	                payrollSummary.setNetSalary(totalAllowance.subtract(totalDeduction));
	                payrollSummary.setMonth(month); // Store month as string (e.g., "JANUARY")
	                payrollSummary.setYear(year);
	            }

	            // Save or update payroll summary
	            payrollSummaryRepository.save(payrollSummary);
	        }
	    }
	}

	
	public Page<PayrollSummary> getPayrollSummary(int page, int size, Integer year, Month enumMonth){	
		generatePayrollSummary();
		
		return payrollSummaryRepository.findAllByYearAndMonth(year,enumMonth,PageRequest.of(page, size));
		
	}
	public void addDeduction(String name, String employeeId, BigDecimal amount, Month month2, int year2) {
	    
	    // Fetch deduction only once
	    Deductions deduction = deductionsRepository.findAllByIdAndMonth(employeeId, month2, year2);
	    Employee employee = employeeService.findByEmployeeId(employeeId);

	    // Check if deduction exists
	    if (deduction != null) {
	        if ("locked".equalsIgnoreCase(deduction.getStatus())) {
	            throw new RuntimeException("Deduction is locked");
	        }
	        
	        // Update existing deduction
	        deduction.setEmployee(employee);
	        deduction.setDeductionAmount(amount);
	        deduction.setDeductionName(name);
	        deduction.setMonth(month2);
	        deduction.setYear(year2);
	    } else {
	        // Create new deduction
	        deduction = new Deductions();
	        deduction.setEmployee(employee);
	        deduction.setDeductionAmount(amount);
	        deduction.setDeductionName(name);
	        deduction.setMonth(month2);
	        deduction.setYear(year2);
	    }

	    // Save the deduction (either updated or new)
	    deductionsRepository.save(deduction);
	}


	public List<Payroll> getAllPayrollsByEmployeeId(String id, Month month, Integer year) {
		return payrollRepository.getPayrollByEmployeeId(id,month,year);
	}

	public Deductions getAllDeductionByEmployeeId(String id, Month month2, int year) {
		return deductionsRepository.findAllByIdAndMonth(id,month2,year);
	}


	public PayrollSummary getPayrollSummaryByEmployeeId(String id,Month month,Integer year) {
		return payrollSummaryRepository.findByEmployeeId(id, month, year);
	}

	



	public void lockPayroll(Month month, Integer year) {
	    List<Payroll> payrolls = payrollRepository.findByMonthAndYear(month, year);
	    List<Deductions> deductions = deductionsRepository.findbyMonthAndYear(month, year);

	    if (payrolls.isEmpty()) {
	        throw new RuntimeException("Payroll Not Found");
	    }

	    // Check if any payroll is already locked
	    boolean isLocked = payrolls.stream()
	        .anyMatch(payroll -> "LOCKED".equalsIgnoreCase(payroll.getStatus()));

	    if (isLocked) {
	        throw new RuntimeException("Payroll Already Locked");
	    }

	    // Lock all payrolls
	    payrolls.forEach(payroll -> payroll.setStatus("LOCKED"));
	    payrollRepository.saveAll(payrolls); // Batch update

	    // Lock all deductions if they exist
	    if (!deductions.isEmpty()) {
	        deductions.forEach(deduction -> deduction.setStatus("LOCKED"));
	        deductionsRepository.saveAll(deductions); // Batch update
	    }
	}

	public BigDecimal getTotalAllowance(Month enumMonth, Integer year) {
		return payrollSummaryRepository.getTotalAllowance(enumMonth,year);
	}

	public BigDecimal getTotalDeduction(Month enumMonth, Integer year) {
		return payrollSummaryRepository.getTotalDeduction(enumMonth,year);
	}

	public void deleteDeduction(String id, String month, String year) {
		int year2=Integer.parseInt(year);
		Month month2=Month.valueOf(month);
		Deductions deductions=getAllDeductionByEmployeeId(id, month2, year2);
		if(deductions==null)
		{
			throw new RuntimeException("Deduction not found");
		}
		else if(deductions!=null && deductions.getStatus().equalsIgnoreCase("locked"))
		{
			throw new RuntimeException("Deduction is Locked");
		}
		else
		{
		deductionsRepository.delete(deductions);
	}

		
	}

	public Page<PayrollSummary> filterPayrollById(int page, int size, String id, Month month, Integer year) {
		// TODO Auto-generated method stub
		return payrollSummaryRepository.filterPayrollById(id,month,year,PageRequest.of(page, size));
	}
}
	

