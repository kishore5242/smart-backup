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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.kishore.sb.GlobalData;
import com.kishore.sb.jpa.SmartStore;
import com.kishore.sb.model.Category;
import com.kishore.sb.model.Command;
import com.kishore.sb.model.CommandInfo;
import com.kishore.sb.model.CommandStatus;
import com.kishore.sb.model.Job;
import com.kishore.sb.service.SmartService;

@Controller
@RequestMapping("/cmd")
public class CommandController {

	private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

	@Autowired
	SmartService smartService;

	@Autowired
	SmartStore store;
	
	@Autowired
	GlobalData data;

	@GetMapping("/")
	public String getAllCommands(Model model) {
		model.addAttribute("getAllCommands", store.getAllCommands());
		return "commands";
	}

	@GetMapping("/new")
	public String newCommand(Model model) {
		model.addAttribute("command", new Command());
		model.addAttribute("jobs", Job.values());
		model.addAttribute("categories", Category.values());
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
		Integer cmdId = store.saveCommand(command);
		data.setCommandInfo(cmdId, CommandStatus.NOT_RUN, "Never ran", 0);
		return "redirect:/cmd/";
	}

	@GetMapping("/run/{id}")
	public @ResponseBody String runCommand(@PathVariable(name = "id") Integer id, Model model) {
		Optional<Command> command = store.getCommand(id);
		if (command.isPresent()) {
			data.resetCommandInfo(id, CommandStatus.STARTED, "Preparing to run...", 0);
			CompletableFuture.runAsync(() -> smartService.runCommand(command.get()));
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Command not found");
		}
		return "submitted";
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
		model.addAttribute("jobs", Job.values());
		model.addAttribute("categories", Category.values());
		model.addAttribute("command", command);
		return "command";
	}
	
	@GetMapping("/info/{id}")
	public @ResponseBody CommandInfo getComments(@PathVariable(name = "id") Integer id, Model model) {
		return data.getCommandInfo(id);
	}

}