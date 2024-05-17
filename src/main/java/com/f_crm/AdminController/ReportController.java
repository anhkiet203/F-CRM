package com.f_crm.AdminController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.f_crm.entity.Customer;
import com.f_crm.entity.ReportForm;
import com.f_crm.service.CustomerService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
public class ReportController {

	@Autowired
	private CustomerService customerService;

	@ModelAttribute("reportForm")
	public ReportForm reportForm() {
		return new ReportForm();
	}

	@GetMapping("/monthly")
	public String getMonthlyStatisticss(Model model, @ModelAttribute("reportForm") ReportForm reportForm) {
		// Kiểm tra nếu không có tháng và năm được chọn thì sử dụng tháng và năm hiện
		if (reportForm.getSelectedMonth() == null) {
			reportForm.setSelectedMonth(LocalDate.now().getMonthValue());
		}
		if (reportForm.getSelectedYear() == null) {
			reportForm.setSelectedYear(LocalDate.now().getYear());
		}
		// Lấy danh sách bệnh nhân theo tháng và năm
		List<Customer> monthlyCustomers = customerService.findCustomersByMonthAndYear(reportForm.getSelectedMonth(),
				reportForm.getSelectedYear());

		model.addAttribute("monthlyCustomers", monthlyCustomers);
		return "Admin/report/report_monthly";
	}

	@ModelAttribute("years")
	public List<Integer> populateYears() {
		// Populate the dropdown with years, e.g., from 2020 to the current year
		int currentYear = YearMonth.now().getYear();
		return IntStream.rangeClosed(2020, currentYear).boxed().collect(Collectors.toList());
	}

	@GetMapping("")
	public String getMonthlyStatistics(Model model, @ModelAttribute("reportForm") ReportForm reportForm) {
		if (reportForm.getSelectedMonth() == null) {
			reportForm.setSelectedMonth(LocalDate.now().getMonthValue());
		}
		if (reportForm.getSelectedYear() == null) {
			reportForm.setSelectedYear(LocalDate.now().getYear());
		}

		List<Customer> monthlyCustomers = customerService.findCustomersByMonthAndYear(reportForm.getSelectedMonth(),
				reportForm.getSelectedYear());

		model.addAttribute("monthlyCustomers", monthlyCustomers);

		// Thêm dữ liệu cho biểu đồ cột
		List<Long> monthlyCustomerCounts = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			Date date = Date.from(YearMonth.of(reportForm.getSelectedYear(), i).atDay(1)
					.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Long count = customerService.countCustomersByMonth(date);

			// Kiểm tra null trước khi thêm vào danh sách
			if (count != null) {
				monthlyCustomerCounts.add(count);
			} else {
				monthlyCustomerCounts.add(null);
			}
		}
		model.addAttribute("monthlyCustomerCounts", monthlyCustomerCounts);

		// Thêm dữ liệu cho biểu đồ tròn (theo năm)
		List<Long> yearlyCustomerCounts = customerService.countCustomersByYearRange(2020, // Năm bắt đầu
				reportForm.getSelectedYear() // Năm kết thúc, có thể sử dụng LocalDate.now().getYear() nếu muốn lấy năm
												// hiện tại
		);
		model.addAttribute("yearlyCustomerCounts", yearlyCustomerCounts);

		Date today = new Date();
		Long dailyCustomerCount = customerService.countCustomersByDateLeads(today);
		model.addAttribute("dailyCustomerCount", dailyCustomerCount);

		model.addAttribute("dailyCustomerCount", dailyCustomerCount);
		// theo tuần
		Long weeklyCustomerCount = customerService.countCustomersByWeek(today);
		model.addAttribute("weeklyCustomerCount", weeklyCustomerCount);
		// theo tháng
		Long monthlyCustomerCount = customerService.countCustomersByMonth(today);
		model.addAttribute("monthlyCustomerCount", monthlyCustomerCount);
		// theo năm
		Long yearlyCustomerCount = customerService.countCustomersByYear(today);
		model.addAttribute("yearlyCustomerCount", yearlyCustomerCount);
		return "Admin/report/report";
	}

	@ModelAttribute("vietnameseMonths")
	public List<String> getVietnameseMonths() {
		// Danh sách các tên tháng bằng tiếng Việt
		String[] vietnameseMonths = new String[] { "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
				"Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12" };
		return List.of(vietnameseMonths);
	}

}
