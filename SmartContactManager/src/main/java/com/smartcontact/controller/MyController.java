package com.smartcontact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("user")
//@RequestMapping("/user")
public class MyController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String home(Model model) {

		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {

		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {

		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	 @GetMapping("/login")
	    public String login(Model model) {
		 	model.addAttribute("title","Login Page");
	        return "login"; // Return the name of the view (login.html in templates)
	    }

	// Handler for registering user

	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,
			BindingResult result,
	        @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
	        RedirectAttributes redirectAttributes,Model model) {
	    try {
	        if (!agreement) {
	            // Redirect with an error message if terms are not agreed upon
	            redirectAttributes.addFlashAttribute("message", new Message("You have not agreed to the terms and conditions", "alert-danger"));
	            model.addAttribute("user", user);
	            return "redirect:/signup"; // Redirect to the signup page
	        }
	        
	        if(result.hasErrors()) {
	        	
	        	System.out.println("ERROR "+result.toString());
	        	
	        	model.addAttribute("user",user);
	        	return "signup";
	        }
	        
	        user.setRole("ROLE_USER");
	        user.setEnabled(true);
	        user.setImage("Default.png");
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        
	        // Save the user and log the results
	        User result1 = this.userRepository.save(user);
	        System.out.println("Agreement " + agreement);
	        System.out.println("User " + user);
	        
	        // Redirect with a success message
	        redirectAttributes.addFlashAttribute("message", new Message("Successfully Registered", "alert-success"));
	        return "redirect:/signup"; // Redirect to the signup page or another page
	        

	    } catch (Exception e) {
	        // Handle exceptions and redirect with an error message
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("message", new Message("Registration failed. Please try again.", "alert-danger"));
	        return "redirect:/signup"; // Redirect to the signup page
	    }
	}

}
