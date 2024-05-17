package com.f_crm.service.iplm;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f_crm.entity.Customer;
import com.f_crm.repository.CustomerRepository;
import com.f_crm.service.CustomerService;

@Service
public class CustomerImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;


    @Override
    public List<Customer> getAllCustomers() {
       return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Integer id) {
       return customerRepository.findById(id).orElseThrow(null);
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }


    @Override
    public Customer getByEmail(String email) {
        return customerRepository.getByEmail(email);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public Customer findById(Integer id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

	// thống kê theo ngày
	@Override
    public Long countCustomersByDateLeads(Date dateLeads) {
        return customerRepository.countCustomersByDateLeads(dateLeads);
    }

	 // thống kê theo tuần 
    @Override
    public Long countCustomersByWeek(Date dateLeads) {
        return customerRepository.countCustomersByWeek(dateLeads);
    }


    // thống kê theo tháng
    @Override
    public Long countCustomersByMonth(Date dateLeads) {
        LocalDate localDate = dateLeads.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();

        if (year == YearMonth.now().getYear()) {
            return customerRepository.countCustomersByMonth(dateLeads);
        } else {
            return 0L; // hoặc trả về null tùy vào yêu cầu của bạn
        }
    }

    // thống kê theo năm 
    @Override
    public Long countCustomersByYear(Date dateLeads) {
        return customerRepository.countCustomersByYear(dateLeads);
    }

    @Override
    public List<Customer> findCustomersByMonthAndYear(Integer month, Integer year) {
        return customerRepository.findCustomersByMonthAndYear(month, year);
    }

    @Override
    public List<Long> countCustomersByYearRange(int startYear, int endYear) {
        return IntStream.rangeClosed(startYear, endYear)
                .mapToObj(year -> customerRepository.countCustomersByYear(year))
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> findCustomersByToday() {
        Date today = new Date();
        return customerRepository.findCustomersByToday(today);
    }

    
}
