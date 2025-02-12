package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Integer> {

	@Query("SELECT c FROM ExpenseCategory c WHERE c.id = :id")
	ExpenseCategory findByCategory(Integer id);

	@Query("SELECT e FROM ExpenseCategory e WHERE LOWER(e.name) = LOWER(:name)")
	ExpenseCategory findByName(@Param("name") String name);


}
