package com.recovery.managementsystem.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.FundSummary;

@Repository
public interface FundSummaryRepository extends JpaRepository<FundSummary, Integer> {

	/* List<FundSummary> findByEmployeeId(String employeeId); */

	FundSummary findByEmployeeId(String employeeId);

	@Query("Select f from FundSummary f where f.employeeId = :employeeId")
	void deleteByEmployeeId(String employeeId);

	@Query("SELECT f FROM FundSummary f WHERE " + "(:employeeId IS NULL OR f.employeeId = :employeeId)")
	Page<FundSummary> getFilteredExpense(String employeeId, Pageable pageable);

	@Query("Select SUM(f.totalCredit) from FundSummary f")
	BigDecimal totalFund();

	@Query("Select SUM(f.totalCredit) from FundSummary f where f.employeeId = :employeeId")
	BigDecimal totalCredit(String employeeId);

	@Query("Select SUM(f.totalDebit) from FundSummary f where f.employeeId = :employeeId")
	BigDecimal totalDebit(String employeeId);

	@Query("SELECT COALESCE(SUM(f.totalCredit), 0) FROM FundSummary f WHERE "
			+ "(:employeeId IS NULL OR f.employeeId = :employeeId) "
			+ "AND (:fromDate IS NULL OR f.entryDate >= :fromDate) "
			+ "AND (:toDate IS NULL OR f.entryDate <= :toDate)")
	BigDecimal getTotalFunds(String employeeId, LocalDate fromDate, LocalDate toDate);

}
