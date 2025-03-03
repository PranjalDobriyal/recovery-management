package com.recovery.managementsystem.repository;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.recovery.managementsystem.model.PayrollSummary;


@Repository
public interface PayrollSummaryRepository extends JpaRepository<PayrollSummary, Integer> {

	@Query("Select p from PayrollSummary p where p.employeeId=:employeeId AND p.month=:month AND p.year=:year")
	PayrollSummary findByEmployeeId(String employeeId, Month month,Integer year);

	 @Query("SELECT p FROM PayrollSummary p WHERE " +
  	       "(:year IS NULL OR p.year = :year) AND " +
  	       "(:month IS NULL OR p.month = :month)" 
  	       )
	Page<PayrollSummary> findAllByYearAndMonth(Integer year, Month month, Pageable pageable);

	 @Query("SELECT COALESCE(SUM(p.totalAllowance), 0) FROM PayrollSummary p WHERE " +
  	       "(:enumMonth IS NULL OR p.month=:enumMonth) AND " +
  	       "(:year IS NULL OR p.year=:year)"
  	       )
	BigDecimal getTotalAllowance(Month enumMonth, Integer year);

	 @Query("SELECT COALESCE(SUM(p.totalDeduction), 0) FROM PayrollSummary p WHERE " +
	  	       "(:enumMonth IS NULL OR p.month=:enumMonth) AND " +
	  	       "(:year IS NULL OR p.year=:year)"
	  	       )
	BigDecimal getTotalDeduction(Month enumMonth, Integer year);

	 
	 @Query("SELECT p FROM PayrollSummary p WHERE p.employeeId = :id "
		       + "AND (:month IS NULL OR p.month = :month) "
		       + "AND (:year IS NULL OR p.year = :year)")
	Page<PayrollSummary> filterPayrollById(String id, Month month, Integer year, Pageable pageable);

	
}
