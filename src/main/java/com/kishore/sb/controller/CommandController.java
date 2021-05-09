package com.kishore.sb.controller;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.kishore.sb.jpa.SmartStore;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.CommandStatus;
import com.kishore.sb.model.OperationData;
import com.kishore.sb.service.SmartService;

@Controller
@RequestMapping("/cmd")
public class CommandController {

	private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

	@Autowired
	SmartService smartService;

	@Autowired
	SmartStore store;

	@GetMapping("/")
	public String getAllCommands(Model model) {
		model.addAttribute("getAllCommands", store.getAllCommands());
		return "commands";
	}

	@GetMapping("/new")
	public String newCommand(Model model) {
		model.addAttribute("command", new Command());
		model.addAttribute("jobTypes", OperationData.JOB_TYPES);
		model.addAttribute("fileTypes", OperationData.FILE_TYPES);
		return "command";
	}

	@GetMapping("/{id}")
	public ModelAndView getCommand(@PathVariable(name = "id") Integer id) {
		ModelAndView mv = new ModelAndView("command");
		Optional<Command> command = store.getCommand(id);
		if (command.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Command with given id not found");
		}
		mv.addObject(command.get());
		return mv;
	}

	@PostMapping("/save")
	public String saveCommand(@ModelAttribute Command command, Model model) {
		command.setStatus(CommandStatus.CREATED);
		command.setComment("Never ran");
		store.saveCommand(command);
		return "redirect:/cmd/";
	}

	@GetMapping("/run/{id}")
	public String runCommand(@PathVariable(name = "id") Integer id, Model model) {
		Optional<Command> command = store.getCommand(id);
		if (command.isPresent()) {

			Command cmd = command.get();
			cmd.setStatus(CommandStatus.STARTED);
			cmd.setComment("Preparing to run");
			store.saveCommand(cmd);

			CompletableFuture.runAsync(() -> smartService.runCommand(cmd));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Command not found");
		}

		model.addAttribute("getAllCommands", store.getAllCommands());
		return "redirect:/cmd/";
	}

	@GetMapping("/delete/{id}")
	public String deleteCommand(@PathVariable(name = "id") Integer id, Model model) {
		store.deleteCommand(id);
		model.addAttribute("getAllCommands", store.getAllCommands());
		return "redirect:/cmd/";
	}
	
	@GetMapping("/edit/{id}")
	public String editCommand(@PathVariable(name = "id") Integer id, Model model) {
		Command command = store.getCommand(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Command not found"));
		model.addAttribute("jobTypes", OperationData.JOB_TYPES);
		model.addAttribute("fileTypes", OperationData.FILE_TYPES);
		model.addAttribute("command", command);
		return "command";
	}

}