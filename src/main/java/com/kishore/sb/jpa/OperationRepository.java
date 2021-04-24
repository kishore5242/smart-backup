package com.kishore.sb.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kishore.sb.entity.OperationEntity;

public interface OperationRepository extends JpaRepository<OperationEntity, Integer> {

}
