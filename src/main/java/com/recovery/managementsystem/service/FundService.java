package com.recovery.managementsystem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.FundManage;
import com.recovery.managementsystem.model.FundSummary;
import com.recovery.managementsystem.repository.EmployeeRepository;
import com.recovery.managementsystem.repository.FundRepository;
import com.recovery.managementsystem.repository.FundSummaryRepository;

@Service
public class FundService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private FundRepository fundRepository;
	
	@Autowired
	private FundSummaryRepository fundSummaryRepository;

	public List<FundManage> findAll() {
	return fundRepository.findAll();
	}
	
	public void generateFundSummary() {
		  
	    List<Employee> employees = employeeRepository.findAllExcept();
	    
	    for (Employee employee : employees) {
	        List<FundManage> funds = fundRepository.findByEmployeeId(employee.getEmployeeId());

	        BigDecimal totalCredit = BigDecimal.ZERO;
	        BigDecimal totalDebit = BigDecimal.ZERO;

	        for (FundManage fund : funds) {
	            if ("credit".equalsIgnoreCase(fund.getAmountType())) {
	                totalCredit = totalCredit.add(fund.getFundAmount());
	            } else if ("debit".equalsIgnoreCase(fund.getAmountType())) {
	                totalDebit = totalDebit.add(fund.getFundAmount());
	            }
	        }

	        BigDecimal totalBalance = totalCredit.subtract(totalDebit);
	        if(fundSummaryRepository.findByEmployeeId(employee.getEmployeeId())!=null)
	        {
	        	FundSummary fundSummary = fundSummaryRepository.findByEmployeeId(employee.getEmployeeId());
		        fundSummary.setTotalCredit(totalCredit);
		        fundSummary.setTotalDebit(totalDebit);
		        fundSummary.setTotalBalance(totalBalance);
		        fundSummaryRepository.save(fundSummary);
	        }

	        else {
	        FundSummary fundSummary = new FundSummary();
	        fundSummary.setEmployeeId(employee.getEmployeeId());
	        fundSummary.setEmployeeName(employee.getEmployeeName());
	        fundSummary.setTotalCredit(totalCredit);
	        fundSummary.setTotalDebit(totalDebit);
	        fundSummary.setTotalBalance(totalBalance);
	        fundSummaryRepository.save(fundSummary);
	    }
	}
	}
	public List<FundSummary> getFundSummary() {
	    generateFundSummary();
	    return fundSummaryRepository.findAll();
	}



	public Page<FundSummary> getFilteredExpenses(int page, int size, String employeeId) {
	
		generateFundSummary();
		return fundSummaryRepository.getFilteredExpense(employeeId, PageRequest.of(page, size));
	}

	public void addFund(Employee employee, FundManage fund, String approvedBy) {
			
		if (fund.getAmountType().equalsIgnoreCase("credit")) {
			
			fund.setPrevBalance(employee.getWalletBalance());
			fund.setNewBalance(employee.getWalletBalance().add(fund.getFundAmount()));
			employee.setWalletBalance(fund.getNewBalance());
		}
		if (fund.getAmountType().equalsIgnoreCase("debit")) 
		{
			fund.setPrevBalance(employee.getWalletBalance());
            fund.setNewBalance(employee.getWalletBalance().subtract(fund.getFundAmount()));
            employee.setWalletBalance(fund.getNewBalance());
        }
		
		fund.setEmployee(employee);
        fund.setGivenBy(approvedBy);
        fund.setCategory(fund.getCategory());
        fund.setFundPurpose(fund.getFundPurpose());
        fund.setPaymentMode(fund.getPaymentMode());
        fund.setStatus("DONE");
        fundRepository.save(fund);
    }

	public List<FundManage> getEmployeeFunds(String id) {
		return fundRepository.findByEmployeeId(id);
	}

	public Page<FundManage> getFilteredFunds(int page, int size,String id, String amountType, String paymentMode,
			String category, LocalDate fromDate, LocalDate toDate) {
		return fundRepository.getFilteredFunds(id,amountType, paymentMode, category,fromDate,toDate, PageRequest.of(page, size));
	}

	public BigDecimal getTotalFund() {
		return fundSummaryRepository.totalFund();
	}

	public BigDecimal getTotalCredit(String employeeId) {
		return fundSummaryRepository.totalCredit(employeeId);
	}

	public BigDecimal getTotalDebit(String employeeId) {
		// TODO Auto-generated method stub
		return fundSummaryRepository.totalDebit(employeeId);
	}

	public FundSummary getFundSummary(String id) {
		return fundSummaryRepository.findByEmployeeId(id);
	}
}
