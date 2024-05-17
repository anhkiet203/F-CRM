package com.f_crm.service;
import java.util.List;
import com.f_crm.entity.Customer;
import java.util.Date;
public interface CustomerService {
    public List<Customer> findAll();
    
   public Customer findById(Integer id); // tìm kiếm theo id 

   public Customer create(Customer customer); // thêm danh sách

   public Customer update(Customer customer); // sửa danh sách

   public void delete(Customer customer);

   Long countCustomersByDateLeads(Date dateLeads); // thống kê theo ngày 

   Long countCustomersByWeek(Date dateLeads); // thống kê theo tuần 

   Long countCustomersByMonth(Date dateLeads); // thống kê theo tháng

   Long countCustomersByYear(Date dateLeads); // thống kê theo năm

   List<Customer> findCustomersByMonthAndYear(Integer month, Integer year);

   List<Long> countCustomersByYearRange(int startYear, int endYear);

   List<Customer> findCustomersByToday();
   
   List<Customer> getAllCustomers();
   Customer getCustomerById(Integer id);
   Customer getByEmail(String email);
   void saveCustomer(Customer customer);
   void updateCustomer(Customer customer);
}
