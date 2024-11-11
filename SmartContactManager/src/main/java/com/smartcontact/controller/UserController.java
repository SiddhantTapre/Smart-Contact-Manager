package com.smartcontact.controller;

import java.io.File;
import java.net.http.HttpClient.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartcontact.dao.ContactRepository;
import com.smartcontact.dao.UserRepository;
import com.smartcontact.entities.Contact;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {

		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);

		Model result = model.addAttribute(user);

	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principle) {

		model.addAttribute("title", "User Dashboard");

		return "/normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openContactForm(Model model) {

		model.addAttribute("title", "Add-Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, RedirectAttributes redirectAttribute) {

		try {

			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			
			
			if (file.isEmpty()) {
				System.out.println("File is Empty");
				contact.setImage("contact.png");

			} else {

				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image is Uploaded");
			}

			contact.setUser(user);

			user.getList().add(contact);

			userRepository.save(user);

			System.out.println(contact);

			redirectAttribute.addFlashAttribute("message",new Message("Contact Successfulluy Added","alert-success"));

			return "redirect:/user/add-contact";

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
			
			redirectAttribute.addFlashAttribute("message",new Message("Failed to Save Contact","alert-danger"));
			return "redirect:/user/add-contact";

		}

//			return "normal/add_contact_form";
	}



	@GetMapping("/show-contacts/{page}")
   public String showContacts(@PathVariable("page") Integer page,Model model,Principal principal) {
		
		model.addAttribute("title","Show User Contacts");
		
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByUserName(userName);
		
		Pageable pageable = PageRequest.of(page,6);
		
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getU_id(),pageable);
		
		model.addAttribute("contacts",contacts);
		model.addAttribute("currentPage",page);
		
		model.addAttribute("totalPages",contacts.getTotalPages());
		
		return "normal/show_contacts";
	   
   }
	
	//for perticular user
	
	@RequestMapping("/{c_id}/contact/")
	public String showContactDetail(@PathVariable("c_id")Integer cId,Model model,Principal principal)
	{
		System.out.println(cId);
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		
		Contact contact = contactOptional.get();
		
		String userName=principal.getName();
		User user=this.userRepository.getUserByUserName(userName);
		
		if(user.getU_id()==contact.getUser().getU_id()) {
			
			model.addAttribute("contact",contact);
			model.addAttribute("title","Show Contacts");
		}
			
		return "normal/contact_detail";
	}
	
	@GetMapping("/delete/{c_id}")
	@Transactional
	public String deleteContact(@PathVariable("c_id")Integer id,Model model,Principal principal,RedirectAttributes redirectAttrubutes) {
		
		
//		String UserName = principal.getName();
//		User user = this.userRepository.getUserByUserName(UserName);
		
		Optional<Contact> contactOptional = this.contactRepository.findById(id);
		Contact contact = contactOptional.get();
		
        User user=this.userRepository.getUserByUserName(principal.getName());
        
        user.getList().remove(contact);
        
        this.userRepository.save(user);
        
		
		if(user.getU_id()==contact.getUser().getU_id())
			
			this.contactRepository.delete(contact);
		
		redirectAttrubutes.addFlashAttribute("message",new Message("Contact Deleted Successfully","success"));
		
		return"redirect:/user/show-contacts/0";
	}
	
	
	@PostMapping("/update-contact/{c_id}")
	public String updateForm(@PathVariable("c_id")Integer cid,Model m) {
		
		m.addAttribute("title","UpdateContact");
		Optional<Contact> contacts = this.contactRepository.findById(cid);
		Contact contact = contacts.get();
		
		m.addAttribute("contact",contact);
		return "normal/update_form";
	}
	
	@PostMapping("/process-update")
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file,Model model,RedirectAttributes redirectAttributes,Principal principal) {
		
		try {
			
//			old contact detail
			
			Contact oldcontactDetail =this.contactRepository.findById(contact.getC_id()).get();
			
			if(!file.isEmpty()) {
				
//				delete old photo
				
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1=new File(deleteFile,oldcontactDetail.getImage());
				file1.delete();

					
//				update new photo
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				contact.setImage(file.getOriginalFilename());
				
			
			}else {
				contact.setImage(oldcontactDetail.getImage());
			}
			
			User user=this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);
			
			this.contactRepository.save(contact);
			
			 redirectAttributes.addFlashAttribute("message",new Message("Your Contact is Updated","success"));
//			 System.out.println("Contact ID: " + contact.getC_id());

			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/"+contact.getC_id()+"/contact/";
	}
	
	@RequestMapping("/profile")
	public String yourprofile(Model model) {
		
		model.addAttribute("title","Profile Page");
		
		return "normal/profile";
	}
	
	@GetMapping("/settings")
	public String openSettings() {
		return "normal/settings";
	}
	
//	change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword")String oldPassword,
			@RequestParam("newPassword")String newPassword,Principal principal,RedirectAttributes redirectAttributes) {
		
		System.out.println("OLD PASSWORD :"+ oldPassword);
		System.out.println("NEW PASSWORD :"+ newPassword);
		
		User currentUser=this.userRepository.getUserByUserName(principal.getName());
		String currentPassword = currentUser.getPassword();
		
		if(bCryptPasswordEncoder.matches(oldPassword, currentPassword)) {			
//			change the password
			
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			redirectAttributes.addFlashAttribute("message",new Message("Password Changed Successfully.","success"));
			
		}else {
			
			redirectAttributes.addFlashAttribute("message",new Message("Old Password Incorrect!!","danger"));
			return "redirect:/user/settings";
		}
		

		return "redirect:/user/index";
	}
	
	
}
