package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


	 Employee findTopByOrderByIdDesc();
	
	@Query("SELECT e FROM Employee e WHERE e.employeeId = :id")
	public Employee findByEmployeeId(String id);

	Employee findByEmail(String email);
    
	@Query("SELECT e FROM Employee e WHERE e.id = :id")
	public Employee getById(Integer id);
	
	

	@Query("SELECT e FROM Employee e WHERE e.employeeType = 'Office'")
	List<Employee> findByOffice();
	
	@Query("SELECT e FROM Employee e WHERE e.employeeType = 'FOS'")
	List<Employee> findByFOS();

	@Query("SELECT e FROM Employee e WHERE e.role != 'ADMIN'")
	public List<Employee> findAllExcept();

}
