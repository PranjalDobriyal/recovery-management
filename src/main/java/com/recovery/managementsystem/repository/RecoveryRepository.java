package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.Recovery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoveryRepository extends JpaRepository<Recovery, Integer> {

	Recovery findByRecoveryId(Integer id);

}
