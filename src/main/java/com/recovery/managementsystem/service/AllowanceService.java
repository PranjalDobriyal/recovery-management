package com.recovery.managementsystem.service;

import com.recovery.managementsystem.model.Allowance;
import com.recovery.managementsystem.repository.AllowanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

@Service
@Transactional
public class AllowanceService {
	
	@Autowired
	private AllowanceRepository allowanceRepository;
	
	public List<Allowance> getAll(){
		return allowanceRepository.findAll();
		
	}

	public void addAllowance(Allowance allowance) {
		if (allowanceRepository.existsByallowanceName(allowance.getAllowanceName())) {
            throw new IllegalArgumentException("Allowance already exist");
        }

        // Check if abbreviation already exists
        if (allowanceRepository.existsByallowanceShort(allowance.getAllowanceShort())) {
            throw new IllegalArgumentException("Allowance abbreviation already exist.");
        }

        allowance.setStatus("ACTIVE");
        allowanceRepository.save(allowance);
    }

	public Allowance getById(Integer id) {
		
		return allowanceRepository.findByallowanceId(id);
	}

	public void updateAllowance(Allowance allowanceUpdate) {
	allowanceRepository.save(allowanceUpdate);
		
	}

	public List<Allowance> findAllById(List<Integer> allowanceIds) {
		return allowanceRepository.findAllById(allowanceIds);
	}

	public void deleteById(Integer id) {
		
		allowanceRepository.deleteById(id);
		
	}
		
	}

