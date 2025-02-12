package com.recovery.managementsystem.service;

import com.recovery.managementsystem.model.Expense;
import com.recovery.managementsystem.model.ExpenseCategory;
import com.recovery.managementsystem.repository.ExpenseCategoryRepository;
import com.recovery.managementsystem.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class ExpenseService {
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
	private ExpenseCategoryRepository expenseCategoryRepository;
	
	 public Page<Expense> getExpenses(int page, int size) {
	        return expenseRepository.findAllByOrderByIdDesc(PageRequest.of(page, size));
	    }


	public List<ExpenseCategory> getAll() {
		
		return expenseCategoryRepository.findAll();
	}

	public void save(Expense expense) {

		expenseRepository.save(expense);
		
	}

	public void savecategory(ExpenseCategory excategory) {
	    String name = excategory.getName().trim(); 

	    ExpenseCategory existingCategory = expenseCategoryRepository.findByName(name);
	    
	    // Check if category exists (case-insensitive)
	    if (existingCategory != null && existingCategory.getName().equalsIgnoreCase(name)) {
	        throw new RuntimeException("Category already exists");
	    }

	    expenseCategoryRepository.save(excategory);
	}

	
	public void deleteById(Integer id) {
		expenseRepository.deleteById(id);
		
	}

	public ExpenseCategory getById(Integer id) {
	return expenseCategoryRepository.findByCategory(id);
	}

	public void updateCategory(ExpenseCategory updatedcategory, ExpenseCategory exCategory) {
		updatedcategory.setName(exCategory.getName());
		updatedcategory.setEditedBy(exCategory.getEditedBy());
		expenseCategoryRepository.save(updatedcategory);
		
	}

	public void deleteCategoryById(Integer id) {
		expenseCategoryRepository.deleteById(id);
		
	}

	public BigDecimal getTotal() {
		
		return expenseRepository.getTotal();
	}


	public Set<String> findAllByName() {
		return expenseRepository.findAllByName();
	}


	public Page<Expense> getFilteredExpenses(int page,int size,String category, String name, LocalDate fromDate, LocalDate toDate, String paymentTypeStr) {
	    Expense.PaymentType paymentType = null;

	    // Convert String to Enum safely
	    if (paymentTypeStr != null && !paymentTypeStr.trim().isEmpty()) {
	        try {
	            paymentType = Expense.PaymentType.valueOf(paymentTypeStr.toUpperCase()); // Case insensitive
	        } catch (IllegalArgumentException e) {
	            System.out.println("Invalid payment type: " + paymentTypeStr);
	            paymentType = null;
	        }
	    }

	    // Fix empty name issue
	    if (name != null && name.trim().isEmpty()) {
	        name = null;
	    }
	    if (category != null && category.trim().isEmpty()) {
	        category = null;
	    }

	    // ✅ Debugging Output
	    System.out.println("Filtering with: Category=" + category + ", Name=" + name + ", From=" + fromDate + ", To=" + toDate + ", PaymentType=" + paymentType);

	    return expenseRepository.filterExpenses(category, name, fromDate, toDate, paymentType,PageRequest.of(page, size));
	}
	
	
	public BigDecimal getFilteredTotalAmount(String category, String name, LocalDate fromDate, LocalDate toDate, String paymentTypeStr) {
	    Expense.PaymentType paymentType = null;

	    // Convert String to Enum safely
	    if (paymentTypeStr != null && !paymentTypeStr.trim().isEmpty()) {
	        try {
	            paymentType = Expense.PaymentType.valueOf(paymentTypeStr.toUpperCase()); // Case insensitive
	        } catch (IllegalArgumentException e) {
	            System.out.println("Invalid payment type: " + paymentTypeStr);
	            paymentType = null;
	        }
	    }


	    if (name != null && name.trim().isEmpty()) {
	        name = null;
	    }
	    if (category != null && category.trim().isEmpty()) {
	        category = null;
	    }


	    return expenseRepository.getFilteredTotalAmount(category, name, fromDate, toDate, paymentType);
	}




}
