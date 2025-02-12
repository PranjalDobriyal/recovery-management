package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer>{

	
	@Query("Select SUM(e.amount) from Expense e")
	BigDecimal getTotal();
	
    Page<Expense> findAllByOrderByIdDesc(Pageable pageable);

    
    @Query("Select e.recipient from Expense e")
	Set<String> findAllByName();
    
    @Query("SELECT e FROM Expense e WHERE " +
    	       "(:category IS NULL OR e.category = :category) AND " +
    	       "(:name IS NULL OR LOWER(e.recipient) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
    	       "(:fromDate IS NULL OR e.expenseDate >= :fromDate) AND " +
    	       "(:toDate IS NULL OR e.expenseDate <= :toDate) AND " +
    	       "(:paymentType IS NULL OR e.paymentType = :paymentType)" +
    	    "ORDER BY e.id DESC"   )
    	Page<Expense> filterExpenses(@Param("category") String category,
    	        @Param("name") String name,
    	        @Param("fromDate") LocalDate fromDate,
    	        @Param("toDate") LocalDate toDate,
    	        @Param("paymentType") Expense.PaymentType paymentType,Pageable pageable
    	);

    
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE " +
    	       "(:category IS NULL OR e.category = :category) AND " +
    	       "(:name IS NULL OR LOWER(TRIM(e.recipient)) LIKE LOWER(CONCAT('%', TRIM(:name), '%'))) AND " +
    	       "(:fromDate IS NULL OR e.expenseDate >= :fromDate) AND " +
    	       "(:toDate IS NULL OR e.expenseDate <= :toDate) AND " +
    	       "(:paymentType IS NULL OR e.paymentType = :paymentType)")
    	BigDecimal getFilteredTotalAmount(
    	        @Param("category") String category,
    	        @Param("name") String name,
    	        @Param("fromDate") LocalDate fromDate,
    	        @Param("toDate") LocalDate toDate,
    	        @Param("paymentType") Expense.PaymentType paymentType
    	);

}
