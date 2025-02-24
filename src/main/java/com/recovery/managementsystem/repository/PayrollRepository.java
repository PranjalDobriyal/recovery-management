package com.recovery.managementsystem.repository;

import java.time.Month;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.recovery.managementsystem.model.Payroll;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Integer> {

    Boolean existsByYearAndMonthAndStatus(Integer year, Month month, String status);

  

    @Transactional
    @Modifying
    @Query("DELETE FROM Payroll p WHERE p.year = :year AND p.month = :month")
    void deleteByMonthAndYear(@Param("year") Integer year, @Param("month") Month month);

    @Query("SELECT p FROM Payroll p WHERE p.employee.employeeId = :id AND p.month = :month AND p.year = :year")
    List<Payroll> getPayrollByEmployeeId(@Param("id") String id, @Param("month") Month month, @Param("year") Integer year);

    @Query("SELECT p FROM Payroll p WHERE p.month = :month AND p.year = :year")
    List<Payroll> findByMonthAndYear(@Param("month") Month month, @Param("year") Integer year);

    
    @Query("Select p from Payroll p where p.employee.employeeId=:employeeId")
	List<Payroll> findByEmployeeId(String employeeId);


    

	
}
