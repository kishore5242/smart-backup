package com.kishore.sb.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.kishore.sb.entity.CommandEntity;
import com.kishore.sb.entity.OperationEntity;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.Operation;

@Service
@Transactional
public class SmartStore {
	
	@Autowired
	CommandRepository commandRepository;
	
	@Autowired
	OperationRepository operationRepository;
	
	public void saveCommand(Command command) {
		
		Optional<OperationEntity> operationEntity = operationRepository.findById(command.getOperation().getId());
		CommandEntity commandEntity;
		if(StringUtils.isEmpty(command.getId())) {
			command.setComment("User created command");
			commandEntity = toEntity(command, operationEntity.get());
		} else {
			commandEntity = commandRepository.findById(command.getId()).get();
			commandEntity.setComment(command.getComment());
			commandEntity.setLhs(command.getLhs());
			//commandEntity.setOperationEntity(operationEntity.get());
			commandEntity.setRhs(command.getRhs());
			commandEntity.setStatus(command.getStatus());
		}

		commandRepository.save(commandEntity);
	}
	
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public List<Command> getAllCommands() {
		return commandRepository.findAll().stream().map(this::fromEntity).collect(Collectors.toList());
	}
	
	public Optional<Command> getCommand(Integer id) {
		return commandRepository.findById(id).map(this::fromEntity);
	}
	
	public void deleteCommand(Integer id) {
		commandRepository.deleteById(id);
	}
	
	@Transactional
	public List<Operation> getAllOperations() {
		return operationRepository.findAll().stream().map(this::fromEntity).collect(Collectors.toList());
	}
	
	@Transactional
	public void saveOperation(Operation operation) {
		operationRepository.save(toEntity(operation));
	}
	
	private CommandEntity toEntity(Command command, OperationEntity operationEntity) {
		CommandEntity entity = new CommandEntity();
		entity.setId(command.getId());
		entity.setLhs(command.getLhs());
		entity.setOperationEntity(operationEntity);
		entity.setRhs(command.getRhs());
		entity.setComment(command.getComment());
		entity.setStatus(command.getStatus());
		return entity;
	}
	
	private Command fromEntity(CommandEntity entity) {
		Command command = new Command();
		command.setId(entity.getId());
		command.setLhs(entity.getLhs());
		command.setRhs(entity.getRhs());
		command.setOperation(fromEntity(entity.getOperationEntity()));
		command.setComment(entity.getComment());
		command.setStatus(entity.getStatus());
		return command;
	}
	
	private OperationEntity toEntity(Operation operation) {
		OperationEntity entity = new OperationEntity();
		entity.setId(operation.getId());
		entity.setName(operation.getName());
		entity.setExtensions(operation.getExtensions());
		entity.setOrganize(operation.isOrganize());
		return entity;
	}
	
	private Operation fromEntity(OperationEntity entity) {
		Operation operation = new Operation();
		operation.setId(entity.getId());
		operation.setName(entity.getName());
		operation.setExtensions(entity.getExtensions());
		operation.setOrganize(entity.isOrganize());
		return operation;
	}

}
