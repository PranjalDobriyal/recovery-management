package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.Allowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowanceRepository extends JpaRepository<Allowance, Integer> {

	boolean existsByallowanceName(String allowanceName);

	boolean existsByallowanceShort(String allowanceShort);

	Allowance findByallowanceId(Integer id);

}
