package com.recovery.managementsystem.service;

import com.recovery.managementsystem.model.Allowance;
import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.EmployeeAllowance;
import com.recovery.managementsystem.repository.EmployeeAllowanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeAllowanceService {

    @Autowired
    private EmployeeAllowanceRepository employeeAllowanceRepository;

    public void updateAllowances(Set<Allowance> newAllowances, 
                                 Map<Integer, BigDecimal> allowanceAmounts, 
                                 Map<Integer, Double> allowancePercentages, 
                                 Employee employee) {
        if (employee == null) {
            throw new RuntimeException("Employee is null and cannot be used.");
        }

        // Fetch current allowances assigned to the employee
        List<EmployeeAllowance> currentAllowances = employeeAllowanceRepository
                .findAllByEmployee_EmployeeId(employee.getEmployeeId());

        // Extract current allowance IDs for easy comparison
        Set<Integer> currentAllowanceIds = currentAllowances.stream()
                .map(empAllow -> empAllow.getAllowance().getAllowanceId())
                .collect(Collectors.toSet());

        // Determine which allowances need to be removed (unchecked)
        List<EmployeeAllowance> toRemove = currentAllowances.stream()
                .filter(empAllow -> !newAllowances.contains(empAllow.getAllowance()))
                .collect(Collectors.toList());

        // Remove unchecked allowances
        if (!toRemove.isEmpty()) {
            employeeAllowanceRepository.deleteAll(toRemove);
        }

        // Create a lookup for current allowances by allowanceId (for updating existing ones)
        Map<Integer, EmployeeAllowance> currentAllowanceLookup = currentAllowances.stream()
                .collect(Collectors.toMap(empAllow -> empAllow.getAllowance().getAllowanceId(), 
                                              empAllow -> empAllow));

        // List to hold new or updated EmployeeAllowance records
        List<EmployeeAllowance> toAddOrUpdate = new ArrayList<>();

        // Iterate over each selected allowance
        for (Allowance allowance : newAllowances) {
            EmployeeAllowance employeeAllowance = currentAllowanceLookup.get(allowance.getAllowanceId());
            if (employeeAllowance == null) {
                // Create a new EmployeeAllowance if it doesn't already exist
                employeeAllowance = new EmployeeAllowance();
                employeeAllowance.setEmployee(employee);
                employeeAllowance.setAllowance(allowance);
            }

            // Update the numeric value based on the allowance type (enum)
            if (allowance.getAllowanceType() == com.recovery.managementsystem.model.Allowance.AllowanceType.AMOUNT) {
                BigDecimal amountValue = allowanceAmounts.get(allowance.getAllowanceId());
                employeeAllowance.setAmount(amountValue);
                // Optionally clear any existing percentage value
                employeeAllowance.setPercentage(null);
            } else if (allowance.getAllowanceType() == com.recovery.managementsystem.model.Allowance.AllowanceType.PERCENTAGE) {
                Double percentageValue = allowancePercentages.get(allowance.getAllowanceId());
                employeeAllowance.setPercentage(percentageValue);
                // Optionally clear any existing amount value
                employeeAllowance.setAmount(null);
            }
            toAddOrUpdate.add(employeeAllowance);
        }

        // Save all new or updated EmployeeAllowance records
        if (!toAddOrUpdate.isEmpty()) {
            employeeAllowanceRepository.saveAll(toAddOrUpdate);
        }
    }

	public BigDecimal findAmount(String employeeId, Integer allowanceId) {
		return employeeAllowanceRepository.findAmount(employeeId,allowanceId);
	}
	public Double findPercent(String employeeId,Integer allowanceId) {
		return employeeAllowanceRepository.findPercentage(employeeId,allowanceId);
	}

	public List<EmployeeAllowance> getEmployeeAllowancesByEmployee_Id(String employeeId) {
		return employeeAllowanceRepository.findAllByEmployee_EmployeeId(employeeId);
	}
}
