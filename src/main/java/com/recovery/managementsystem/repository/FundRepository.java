package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.FundManage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface FundRepository extends JpaRepository<FundManage, Integer> {
	
	@Query("SELECT f FROM FundManage f WHERE " +
		       "(:employeeId IS NULL OR f.employee.employeeId = :employeeId) AND " +
		       "(:fundType IS NULL OR f.fundType = :fundType) AND " +
		       "(:fromDate IS NULL OR f.approvalDate >= :fromDate) AND " +
		       "(:toDate IS NULL OR f.approvalDate <= :toDate) " + 
		       "ORDER BY f.id DESC")
	Page<FundManage> filterExpenses(String employeeId, FundManage.FundType fundType, LocalDate fromDate, LocalDate toDate,Pageable pageable);

	
}
