package com.smartcontact.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;
import com.smartcontact.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {

	Random random = new Random(1000);

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@RequestMapping("/forgot")
	public String openEmailForm() {

		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session,
			RedirectAttributes redirectAttributes) {

		System.out.println("Email : " + email);

//		generating otp of 4 digits

		int otp = random.nextInt(9999);

		System.out.println("OTP : " + otp);

//		write code for send opt to email

		String subject = "OTP from SmartContactManager";
		String message = "" + "<div style='border:1px solid #e2e2e2; padding:20px'>" + "<h1>" + "OTP is " + "<b>" + otp
				+ "</n>" + "</h1>" + "</div>";

		String to = email;

		boolean flag = this.emailService.sendEmail(subject, message, to);

		if (flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			
			redirectAttributes.addFlashAttribute("message",new Message("OTP Send to Your Email","success"));
			
			return "redirect:/verify-otp";
		} else {

			redirectAttributes.addFlashAttribute("message", new Message("Check Your Email!!!!", "danger"));

			return "forgot_email_form";
		}

	}
	
	@GetMapping("/verify-otp")
	public String showVerifyOtpForm() {
	    return "verify_otp"; // Ensure this matches the name of your Thymeleaf template
	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session,
		RedirectAttributes redirectAttributes) {

		int myOtp = (int) session.getAttribute("myotp");
		String email = (String) session.getAttribute("email");

		if (myOtp == otp) {

			User user = this.userRepository.getUserByUserName(email);

			if (user == null) {
//				send error message
				redirectAttributes.addFlashAttribute("message", new Message("User Does not Exits with this Email!", "danger"));
				return "redirect:/forgot";
				
			} else {
//				send change password form
				
			}
			
			return "password_change_form";

			
		} else {
			redirectAttributes.addFlashAttribute("message",new Message("You have Entered Wrong OTP", "danger"));
			return "redirect:/verify-otp";
		}

	}
	
	
//	change password
	
	@RequestMapping("/change-password")
	public String changePassword(@RequestParam("newPassword")String newPassword,HttpSession session,RedirectAttributes redirectAttributes) {
		
		String email = (String) session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(user);
		
		
		redirectAttributes.addFlashAttribute("message",new Message("Password Changed...","success"));
		
		return "redirect:/login";
	}

}
