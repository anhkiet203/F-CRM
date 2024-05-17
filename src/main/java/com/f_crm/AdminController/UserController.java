package com.f_crm.AdminController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.f_crm.entity.Authority;
import com.f_crm.entity.ChangePass;
import com.f_crm.entity.ExpertDoctor;
import com.f_crm.entity.Role;
import com.f_crm.entity.Users;
import com.f_crm.repository.AuthorityRepository;
import com.f_crm.repository.ExpertDoctorRepository;
import com.f_crm.repository.RoleRepository;
import com.f_crm.repository.UserRepository;
import com.f_crm.service.ExpertDoctorService;
import com.f_crm.service.MailService;
import com.f_crm.service.UserService;

@MultipartConfig
@Controller
public class UserController {

	@Autowired
	ExpertDoctorService doctorService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AuthorityRepository authorityRepository;

	@Autowired
	ExpertDoctorRepository expertDoctorRepository;

	@Autowired
	MailService mailService;

	@Autowired
	HttpSession session;

	@Autowired
	BCryptPasswordEncoder pe;

	private static String UPLOAD_DIRECTORY = System.getProperty("user.dir")
			+ "/src/main/resources/static/assets/uploads/";

	@GetMapping("/user")
	public String userlist(Model model) {
		List<Users> listuser = userRepository.findAlluser();
		model.addAttribute("users", listuser);
		return "Admin/user/list";
	}

	@GetMapping("/userform")
	public String userform(@ModelAttribute("user") Users user, Model model) {
		return "Admin/user/form";
	}

	@PostMapping("/register/save")
	public String addUser(@Valid @ModelAttribute("user") Users user, BindingResult result,
			@RequestParam("image") MultipartFile photo, Model model) {

		// Kiểm tra lỗi cho trường 'expertdoctor'
		if (user.getExpertdoctor().getId() == null) {
			model.addAttribute("expertdoctor", "Vui lòng chọn chuyên ngành!");
			return "Admin/user/form";
		}

		if (photo != null && !photo.isEmpty()) {
			try {
				String fileName = photo.getOriginalFilename();
				Path fileNameAnPath = Paths.get(UPLOAD_DIRECTORY, fileName);
				Files.write(fileNameAnPath, photo.getBytes());
				user.setPhoto(fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			model.addAttribute("photo_message", "Vui lòng chọn ảnh!");
			return "Admin/user/form";
		}
		if (result.hasErrors()) {

			return "Admin/user/form";
		}

		if (userRepository.getByUsername(user.getUsername()) != null) {
			model.addAttribute("username", "Tên đăng nhập đã tồn tại!");
			return "Admin/user/form";
		}
		if (userRepository.getByEmail(user.getEmail()) != null) {
			model.addAttribute("email", "Email đã tồn tại!");
			return "Admin/user/form";
		}

		String pass = user.getPassword();
		String passBcr = pe.encode(pass);
		user.setPassword(passBcr);

		userRepository.save(user);

		Role staff = roleRepository.findById("STAFF").orElseGet(() -> {
			Role newRole = new Role();
			newRole.setId("STAFF");
			return roleRepository.save(newRole);
		});

		Authority au = new Authority();
		au.setUser(user);
		au.setRole(staff);
		authorityRepository.save(au);

		mailService.add(memeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(memeMessage);
			try {
				helper.setTo(user.getEmail());
				helper.setSubject("Bệnh Viện Mắt Việt An Xin Chào");
				helper.setText("Đây là tài khoản đăng nhập của bạn: \nTên đăng nhập: " + user.getUsername()
						+ ". \nMật khẩu: " + pass + ".");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		});

		model.addAttribute("create", "Thêm mới thành công!");

		return ("Admin/user/form");
	}

	@ModelAttribute("expertdoctors")
	public Iterable<ExpertDoctor> getAllExpertdoctor() {
		return expertDoctorRepository.findActive();
	}

	@GetMapping("/edit/{id}")
	public String Editform(@PathVariable("id") String id, Model model) {
		Users user = userService.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Username không tồn tại: " + id));
		model.addAttribute("user", user);
		return "Admin/user/formUpdate";
	}

	@PostMapping("/edit/{username}")
	public String UpdateUser(@PathVariable("username") String username, @ModelAttribute("user") Users user,
			@RequestParam("image") MultipartFile photo, Model model) {

		// bắt lỗi
		if (user.getFullname().isBlank()) {
			model.addAttribute("fullname", "Họ và tên không được để trống!");
			return "Admin/user/formUpdate";
		}
		if (user.getAddress().isBlank()) {
			model.addAttribute("address", "Địa chỉ không được để trống!");
			return "Admin/user/formUpdate";
		}
		if (user.getPhone().isBlank()) {
			model.addAttribute("phone", "Số điện thoại không được để trống!");
			return "Admin/user/formUpdate";
		}
		if (user.getPhone().length() < 10 || user.getPhone().length() > 10) {
			model.addAttribute("phone", "Số điện thoại phải 10 số!");
			return "Admin/user/formUpdate";
		}

		String regexp = "^(0[3|5|7|8|9])+([0-9]{8})";
		if (!user.getPhone().matches(regexp)) {
			model.addAttribute("phone", "Sai định dạng số điện thoại!");
			return "Admin/user/formUpdate";
		}

		if (user.getEmail().isBlank()) {
			model.addAttribute("email", "Email không được để trống!");
			return "Admin/user/formUpdate";
		}
		if (!user.getEmail().matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$")) {
			model.addAttribute("email", "Sai định dạng email!");
			return "Admin/user/formUpdate";
		}
		// Kiểm tra lỗi cho trường 'expertdoctor'
		if (user.getExpertdoctor().getId() == null) {
			model.addAttribute("expertdoctor", "Vui lòng chọn chuyên ngành!");
			return "Admin/user/formUpdate";
		}
		Users currentUser = userService.getByUsername(username);

		if (photo != null && !photo.isEmpty()) {
			String fileName = photo.getOriginalFilename();
			user.setPhoto(fileName);
		} else {
			String photoname = currentUser.getPhoto();
			user.setPhoto(photoname);
		}

		String pass = currentUser.getPassword();
		System.out.println("Pass: " + pass);
		user.setPassword(pass);

		userRepository.save(user);
		model.addAttribute("update", "Cập nhật thành công!");

		return "Admin/user/formUpdate";
	}

	@GetMapping("/delete/{username}")
	public String DeleteUser(@PathVariable("username") String username, Model model) {
		Users user = userService.findById(username)
				.orElseThrow(() -> new IllegalArgumentException("Username không tồn tại: " + username));
		userService.delete(username);
		return "redirect:/user";
	}

	@PostMapping("/sigin/save")
	public String sigin(Model model, @ModelAttribute("user") Users user) {
		Users currentUser = userRepository.getByUsername(user.getUsername());
		if (currentUser != null) {
			session.setAttribute("currentUsername", currentUser.getUsername());
		}

		session.setAttribute("user", currentUser);
		System.out.println("user");
		return "redirect:/home";
	}

	@RequestMapping("/sigin")
	public String error(Model model, @RequestParam(required = false) String error) {
		if (error != null) {
			model.addAttribute("errors", "Đăng nhập thất bại!");
		}
		return "User/layout/signin";
	}

	@RequestMapping("/auth/access/denied")
	public String denied(Model model) {
		model.addAttribute("roleError", "Bạn không có quyền try cập!");
		return "User/layout/signin";
	}

	@GetMapping("/logout")
	public String logout() {
		return "forward:/signin";
	}

	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestParam("email") String email, Model model) {

		Users users = userRepository.getByEmail(email);
		String username = (users != null) ? users.getUsername() : null;
		if (username != null) {
			int leftLimit = 97; // 'a'
			int rightLimit = 122; // 'z'
			int len = 8;
			Random random = new Random();
			StringBuilder buffer = new StringBuilder(len);
			for (int i = 0; i < len; i++) {
				int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
				buffer.append((char) randomLimitedInt);
			}
			String generateString = buffer.toString();
			System.out.println("Random String:" + generateString);
			Users user = userRepository.getByEmail(email);
			String newpass = pe.encode(generateString);
			System.out.println("PE String:" + newpass);
			user.setPassword(newpass);

			userRepository.save(user);
			try {
				sendEmail(email, generateString);
				model.addAttribute("message", "Vui lòng xem gmail để lấy lại mật khẩu!");
				return "User/layout/forgot_password";
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			model.addAttribute("message", "Email không tồn tại");
			return "User/layout/forgot_password";
		}
		return "User/layout/forgot_password";
	}

	private void sendEmail(String email, String password) {
		mailService.add(mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			try {
				helper.setTo(email);
				helper.setSubject("Bệnh Viện Mắt Việt An Đà Nẵng Xin Chào");
				helper.setText(
						"Bạn đã quên mật khẩu và đã yêu cầu một mật khẩu mới! <br/>Đây là mật khẩu đăng nhập của bạn: <span style='font-size: 18px; color: red'>"
								+ password + "</span>.",
						true);
			} catch (MessagingException e) {

			}
		});
	}

	@GetMapping("/change")
	public String change(Model model, Authentication authentication) {
		String username = authentication.getName();
		model.addAttribute("username", username);
		model.addAttribute("changePasswordForm", new ChangePass());
		return "User/layout/changepass";
	}

	@PostMapping("/change-password")
	public String changePassword(Model model, @ModelAttribute("changePasswordForm") @Valid ChangePass changePass,
			BindingResult bindingResult, Authentication authentication) {
		String username = authentication.getName();
		Users user = userRepository.getByUsername(username);
		if (bindingResult.hasErrors()) {
			model.addAttribute("username", user);
			return "User/layout/changepass";
		}
		model.addAttribute("username", user);

		String oldPasswordFromUser = changePass.getOldPassword(); // Không cần mã hóa mật khẩu cũ từ người dùng
		System.out.println("PassOle>>" + oldPasswordFromUser);

		String oldPasswordFromDatabase = user.getPassword();
		System.out.println("PassDatabase>>" + oldPasswordFromDatabase);

		System.out.println("Length User: " + oldPasswordFromUser.length());
		System.out.println("Length Database: " + oldPasswordFromDatabase.length());

		System.out.println("Mk có bằng không: " + pe.matches(oldPasswordFromUser, oldPasswordFromDatabase));

		if (!pe.matches(oldPasswordFromUser, oldPasswordFromDatabase)) {
			model.addAttribute("messageold", "Mật khẩu hiện tại chưa đúng!");
			return "User/layout/changepass";
		}
		if (!changePass.getNewPassword().equals(changePass.getConfirmPassword())) {
			model.addAttribute("message", "Xác nhận mật khẩu không đúng");
			return "User/layout/changepass";
		}
		if (changePass.getNewPassword().equals(changePass.getOldPassword())) {
			model.addAttribute("messagenew", "Mật khẩu mới không được trùng với mật khẩu hiện tại!");
			return "User/layout/changepass";
		}
		userService.changePass(user, changePass.getNewPassword());
		String newpass = changePass.getNewPassword();
		String newpe = pe.encode(newpass);
		System.out.println("PE String:" + newpass);
		user.setPassword(newpe);

		userRepository.save(user);
		model.addAttribute("pass","Thay đổi mật khẩu thành công!");
		return "User/layout/changepass";
	}
}
