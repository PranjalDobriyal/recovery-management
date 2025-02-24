package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.FundManage;
import com.recovery.managementsystem.model.FundSummary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FundRepository extends JpaRepository<FundManage, Integer> {

	@Query("Select f from FundManage f where f.employee.employeeId = :employeeId")
	List<FundManage> findByEmployeeId(String employeeId);

	@Query("SELECT f FROM FundManage f WHERE f.employee.employeeId = :id " +
		       "AND (:amountType IS NULL OR f.amountType = :amountType) " +
		       "AND (:paymentMode IS NULL OR f.paymentMode = :paymentMode) " +
		       "AND (:category IS NULL OR f.category = :category) " +
		       "AND (:fromDate IS NULL OR f.entryDate >= :fromDate) " +
		       "AND (:toDate IS NULL OR f.entryDate <= :toDate)")
	Page<FundManage> getFilteredFunds(@Param("id") String id,
            @Param("amountType") String amountType,
            @Param("paymentMode") String paymentMode,
            @Param("category") String category,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);

	
	
}