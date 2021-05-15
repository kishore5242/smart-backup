package com.kishore.sb.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.kishore.sb.entity.CommandEntity;
import com.kishore.sb.entity.OperationEntity;
import com.kishore.sb.model.Category;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.Job;
import com.kishore.sb.model.Operation;

@Service
@Transactional
public class SmartStore {

	@Autowired
	CommandRepository commandRepository;

	@Autowired
	OperationRepository operationRepository;

	public Integer saveCommand(Command command) {
		OperationEntity operationEntity = toEntity(command.getOperation());
		operationRepository.save(operationEntity);

		CommandEntity commandEntity = toEntity(command, operationEntity);
		return commandRepository.save(commandEntity).getId();
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
		return entity;
	}

	private Command fromEntity(CommandEntity entity) {
		Command command = new Command();
		command.setId(entity.getId());
		command.setLhs(entity.getLhs());
		command.setRhs(entity.getRhs());
		command.setOperation(fromEntity(entity.getOperationEntity()));
		return command;
	}

	private OperationEntity toEntity(Operation operation) {
		OperationEntity entity = new OperationEntity();
		entity.setId(operation.getId());
		entity.setName(operation.getName());
		entity.setJob(operation.getJob().toString());
		entity.setCategories(String.join(",",
				operation.getCategories().stream().map(Category::toString).collect(Collectors.toSet())));
		entity.setAvoidDuplication(operation.getAvoidDuplication());
		entity.setDeleteFromSource(operation.getDeleteFromSource());
		return entity;
	}

	private Operation fromEntity(OperationEntity entity) {
		Operation operation = new Operation();
		operation.setId(entity.getId());
		operation.setName(entity.getName());
		operation.setJob(Job.valueOf(entity.getJob()));
		
		Set<Category> categories = new HashSet<>();
		String[] catArray = entity.getCategories().split(",");
		for(String category: catArray) {
			categories.add(Category.valueOf(category));
		}
		operation.setCategories(categories);
		operation.setAvoidDuplication(entity.getAvoidDuplication());
		operation.setDeleteFromSource(entity.getDeleteFromSource());
		return operation;
	}

}
