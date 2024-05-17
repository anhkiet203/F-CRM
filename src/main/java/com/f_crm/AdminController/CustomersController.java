package com.f_crm.AdminController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.f_crm.entity.Authority;
import com.f_crm.entity.Customer;
import com.f_crm.entity.ExpertDoctor;
import com.f_crm.entity.Method;
import com.f_crm.entity.Users;
import com.f_crm.repository.AuthorityRepository;
import com.f_crm.repository.CustomerRepository;
import com.f_crm.repository.ExpertDoctorRepository;
import com.f_crm.repository.MethodRepository;
import com.f_crm.repository.UserRepository;
import com.f_crm.service.CustomerService;
import com.f_crm.service.ExpertDoctorService;
import com.f_crm.service.MethodService;
import com.f_crm.service.UserService;

@Controller
public class CustomersController {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerService customerService;

	@Autowired
	ExpertDoctorService doctorService;

	@Autowired
	ExpertDoctorRepository expertDoctorRepository;

	@Autowired
	MethodRepository methodRepository;

	@Autowired
	MethodService methodService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	AuthorityRepository authorityRepository;

	@GetMapping("/customers")
	public String customerlist(Model model) {
		List<Customer> cus = customerRepository.getfindAll();
		List<Method> me = methodService.findAll();

		model.addAttribute("customer", new Customer());
		model.addAttribute("cus", cus);

		return "Admin/customers/list";
	}

	@GetMapping("/customersform")
	public String showAddCustomerForm(@ModelAttribute("customer") Customer customer, Model model) {
		List<Customer> cus = customerRepository.getfindAll();
		List<Method> me = methodService.findAll();
		model.addAttribute("customer", new Customer());
		model.addAttribute("cus", cus);

		return "Admin/customers/form";
	}

	@PostMapping("/customers/add")
	public String addCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model) {
		String doctor = customer.getUser().getUsername();
		Users authority = userRepository.getByUsername(doctor);

		if (result.hasErrors()) {
			if (customer.getBirthday() == null) {
				model.addAttribute("birthday", "Vui lòng nhập ngày tháng năm sinh");

			}
			if (customer.getArrival_method().isBlank()) {
				model.addAttribute("arr", "Vui lòng chọn phương thức đến!");

			}
			if (customer.getMethods().getId() == null) {
				model.addAttribute("me", "Vui lòng chọn phương pháp điều trị!");

			}
			if (authority == null) {
				model.addAttribute("us", "Vui lòng chọn bác sĩ phụ trách!");

			}
			if (customer.getSurgery_date() == null) {
				model.addAttribute("su", "Vui lòng chọn ngày phẩu thuật!");
			}
			if (customer.getTreatment_date() == null) {
				model.addAttribute("tr", "Vui lòng chọn ngày khám!");
			}
			if (customer.getAppointment_date() == null) {
				model.addAttribute("ap", "Vui lòng chọn ngày hẹn!");
			}
			return "Admin/customers/form";
		}

		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, -1); // Trừ đi 1 ngày từ ngày hiện tại

		if (currentDate.getTime() != null && customer.getBirthday().after(currentDate.getTime())) {
			model.addAttribute("birthday", "Ngày tháng năm sinh phải sau ngày hiện tại");
			return "Admin/customers/form";
		}
		if (customer.getAppointment_date() != null && currentDate.getTime() != null
				&& customer.getAppointment_date().before(currentDate.getTime())) {
			model.addAttribute("ap", "Ngày hẹn phải sau ngày hiện tại");
			return "Admin/customers/form";
		}
		if (customer.getTreatment_date() != null && currentDate.getTime() != null
				&& customer.getTreatment_date().before(currentDate.getTime())) {
			model.addAttribute("tr", "Ngày đến khám phải sau ngày hiện tại");
			return "Admin/customers/form";
		}
		// kiểm tra ngày phẫu thuật
		if (customer.getSurgery_date() != null && currentDate.getTime() != null
				&& customer.getSurgery_date().before(currentDate.getTime())) {
			model.addAttribute("su", "Ngày phẫu thuật phải sau ngày hiện tại");
			return "Admin/customers/form";
		}

		// Kiểm tra email
		if (customerRepository.getByEmail(customer.getEmail()) != null) {
			model.addAttribute("email", "Email đã tồn tại!");
			return "Admin/customers/form";
		}

		// Gán user_id vào đối tượng Customer
		customer.setUser(authority);

		customerRepository.save(customer);

		model.addAttribute("create", "Thêm mới thành công");
		return "Admin/customers/form";
	}

	@GetMapping("/customers/edit/{id}")
	public String editCus(@PathVariable Integer id, @ModelAttribute("customer") Customer customer, Model model) {
		Customer cus = customerRepository.getFindById(id);
		model.addAttribute("customer", cus);
		return "Admin/customers/formUpdate";
	}

	@PostMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable Integer id, @Valid @ModelAttribute("customer") Customer customer,
			BindingResult result, Model model) {
		Customer existingCustomer = customerService.getCustomerById(id);
		if (existingCustomer == null) {
			// Xử lý khi không tìm thấy khách hàng
			return "Admin/customers/formUpdate";
		}
		String doctor = customer.getUser().getUsername();
		Users authority = userRepository.getByUsername(doctor);
		if (result.hasErrors()) {
			if (customer.getBirthday() == null) {
				model.addAttribute("birthday", "Vui lòng nhập ngày tháng năm sinh");

			}
			if (customer.getArrival_method().isBlank()) {
				model.addAttribute("arr", "Vui lòng chọn phương thức đến!");

			}
			if (customer.getMethods().getId() == null) {
				model.addAttribute("me", "Vui lòng chọn phương pháp điều trị!");

			}
			if (authority == null) {
				model.addAttribute("us", "Vui lòng chọn bác sĩ phụ trách!");

			}
			if (customer.getSurgery_date() == null) {
				model.addAttribute("su", "Vui lòng chọn ngày phẩu thuật!");
			}
			if (customer.getTreatment_date() == null) {
				model.addAttribute("tr", "Vui lòng chọn ngày khám!");
			}
			if (customer.getAppointment_date() == null) {
				model.addAttribute("ap", "Vui lòng chọn ngày hẹn!");
			}
			return "Admin/customers/formUpdate";
		}
		Calendar currentDate = Calendar.getInstance();
		currentDate.add(Calendar.DATE, -1); // Trừ đi 1 ngày từ ngày hiện tại

		if (currentDate.getTime() != null && customer.getBirthday().after(currentDate.getTime())) {
			model.addAttribute("birthday", "Ngày tháng năm sinh phải sau ngày hiện tại");
			return "Admin/customers/formUpdate";
		}
		if (customer.getAppointment_date() != null && currentDate.getTime() != null
				&& customer.getAppointment_date().before(currentDate.getTime())) {
			model.addAttribute("ap", "Ngày hẹn phải sau ngày hiện tại");
			return "Admin/customers/formUpdate";
		}
		if (customer.getTreatment_date() != null && currentDate.getTime() != null
				&& customer.getTreatment_date().before(currentDate.getTime())) {
			model.addAttribute("tr", "Ngày đến khám phải sau ngày hiện tại");
			return "Admin/customers/formUpdate";
		}
		// kiểm tra ngày phẫu thuật
		if (customer.getSurgery_date() != null && currentDate.getTime() != null
				&& customer.getSurgery_date().before(currentDate.getTime())) {
			model.addAttribute("su", "Ngày phẫu thuật phải sau ngày hiện tại");
			return "Admin/customers/formUpdate";
		}
		customerService.updateCustomer(customer);
		model.addAttribute("update", "Cập nhật thành công!");
		return "Admin/customers/formUpdate";
	}

	@GetMapping("/customers/delete/{id}")
	public String confirmDeleteCustomer(@PathVariable Integer id, @RequestParam(required = false) boolean confirm,
			RedirectAttributes redirectAttributes) {
		Customer cus = customerRepository.getFindById(id);
		cus.setActive(false);
		customerRepository.save(cus);
		return "redirect:/customers";
	}

	@ModelAttribute("usersDoctor")
	public Iterable<Authority> getAllAuthority() {
		return authorityRepository.findByDoctor();
	}

	@ModelAttribute("methods")
	public Iterable<Method> getAllMethod() {
		return methodRepository.findActive();
	}

	@GetMapping("/customerslist")
	public String customerlistbenhnhan(Model model) {
		model.addAttribute("customers", customerService.findCustomersByToday());
		return "Admin/customers/listBenhnhan";
	}

}
