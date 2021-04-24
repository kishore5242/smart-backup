package com.kishore.sb.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kishore.sb.entity.CommandEntity;

public interface CommandRepository extends JpaRepository<CommandEntity, Integer> {

}
