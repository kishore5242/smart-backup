package com.kishore.sb.controller;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.kishore.sb.model.Operation;
import com.kishore.sb.service.SmartService;

@Controller
@RequestMapping("/cmd")
public class CommandController {

	private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

	@Autowired
	SmartService smartService;

	@Autowired
	SmartStore store;

	@Value("${welcome.message}")
	private String message;

	@GetMapping("/")
	public String getAllCommands(Model model) {		
		model.addAttribute("getAllCommands", store.getAllCommands());
		return "commands";
	}
	
	@GetMapping("/load")
	public String loadOperations(Model model) {		
		
		Operation copyOp = new Operation();
		copyOp.setId(100);
		copyOp.setName("Copy all");
		copyOp.setOrganize(false);
		copyOp.setExtensions("all");
		
		Operation copyImgOp = new Operation();
		copyImgOp.setId(200);
		copyImgOp.setName("Copy images");
		copyImgOp.setOrganize(false);
		copyImgOp.setExtensions("jpg,jpeg,png,gif");
		
		store.saveOperation(copyOp);
		store.saveOperation(copyImgOp);
		
		return "redirect:/cmd/";
	}

	@GetMapping("/new")
	public String newCommand(Model model) {
		model.addAttribute("operations", store.getAllOperations());
		model.addAttribute("command", new Command());
		model.addAttribute("getAllOperations", store.getAllOperations());
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
		command.setStatus("created");
		store.saveCommand(command);		
		model.addAttribute("message", message);
		model.addAttribute("getAllCommands", store.getAllCommands());
		return "redirect:/cmd/";
	}

	@GetMapping("/run/{id}")
	public String runCommand(@PathVariable(name = "id") Integer id, Model model) {
		Optional<Command> command = store.getCommand(id);
		if(command.isPresent()) {
			
			Command cmd = command.get();
			cmd.setStatus("started");
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

}