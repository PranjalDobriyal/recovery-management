package com.recovery.managementsystem.repository;
import java.time.Month;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.recovery.managementsystem.model.Deductions;



@Repository
public interface DeductionsRepository extends JpaRepository<Deductions, Integer>
{	
	@Query("Select d from Deductions d where d.employee.employeeId=:employeeId AND d.month=:month AND d.year=:year")
     Deductions  findAllByIdAndMonth(String employeeId, Month month, Integer year);

	@Query("Select d from Deductions d where d.month=:month AND d.year=:year")
	List<Deductions> findbyMonthAndYear(Month month, Integer year);

	

	

}
