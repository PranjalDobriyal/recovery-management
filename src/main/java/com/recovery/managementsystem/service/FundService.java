package com.recovery.managementsystem.service;

import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.FundManage;
import com.recovery.managementsystem.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FundService {
	
	@Autowired
	private FundRepository fundRepository;

	public List<FundManage> findAll() {
	return fundRepository.findAll();
		
	}

	public void addFund(Employee employee, FundManage fund, String id) {
	    // Set Employee
	    fund.setEmployee(employee);

	    // Handle null values safely
	    BigDecimal fundAmount = fund.getFundAmount() != null ? fund.getFundAmount() : BigDecimal.ZERO;
	    BigDecimal currentCredit = fund.getCreditAmount() != null ? fund.getCreditAmount() : BigDecimal.ZERO;

	    // Update Credit Amount
	    fund.setCreditAmount(fundAmount.add(currentCredit));

	    // Assign the session ID or Admin ID who approved it
	    fund.setApprovedBy(id);

	    // Save fund to the database
	    fundRepository.save(fund);
	}

	public Page<FundManage> getFilteredExpenses(int page, int size, String employeeId, String fundTypestr, LocalDate fromDate,
			LocalDate toDate) {
		FundManage.FundType fundType = null;

	    // Convert String to Enum safely
	    if (fundTypestr != null && !fundTypestr.trim().isEmpty()) {
	        try {
	        	fundType = FundManage.FundType.valueOf(fundTypestr); // Case insensitive
	        } catch (IllegalArgumentException e) {
	            System.out.println("Invalid payment type: " + fundType);
	            fundType = null;
	        }
	    }
		
		 if (employeeId != null && employeeId.trim().isEmpty()) {
		        employeeId = null;
		    }
		    return fundRepository.filterExpenses(employeeId, fundType, fromDate, toDate,PageRequest.of(page, size));
		 
	}

}
