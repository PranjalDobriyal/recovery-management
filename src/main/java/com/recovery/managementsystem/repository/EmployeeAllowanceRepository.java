package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.EmployeeAllowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmployeeAllowanceRepository extends JpaRepository<EmployeeAllowance, Integer>{
	
	
    List<EmployeeAllowance> findAllByEmployee_EmployeeId(String employeeId);

    @Query("SELECT e.amount FROM EmployeeAllowance e WHERE e.employee.employeeId = :employeeId AND e.allowance.allowanceId = :allowanceId")
    BigDecimal findAmount(@Param("employeeId") String employeeId, @Param("allowanceId") Integer allowanceId);

    @Query("SELECT e.percentage FROM EmployeeAllowance e WHERE e.employee.employeeId = :employeeId AND e.allowance.allowanceId = :allowanceId")
    Double findPercentage(@Param("employeeId") String employeeId, @Param("allowanceId") Integer allowanceId);



}
