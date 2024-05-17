package com.f_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

import com.f_crm.entity.Authority;
import com.f_crm.entity.Customer;
import org.springframework.stereotype.Repository;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{


    // thống kê theo ngày
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.dateLeads = :dateLeads")
    Long countCustomersByDateLeads(@Param("dateLeads") Date dateLeads);

    // thống kê theo tuần
    @Query("SELECT COUNT(c) FROM Customer c WHERE DATEPART(WEEK, c.dateLeads) = DATEPART(WEEK, :dateLeads)")
    Long countCustomersByWeek(@Param("dateLeads") Date dateLeads);
    
    // thống kê theo tháng
    @Query("SELECT COUNT(c) FROM Customer c WHERE MONTH(c.dateLeads) = MONTH(:dateLeads) AND YEAR(c.dateLeads) = YEAR(:dateLeads)")
    Long countCustomersByMonth(@Param("dateLeads") Date dateLeads);

    // thống kê theo năm 
    @Query("SELECT COUNT(c) FROM Customer c WHERE YEAR(c.dateLeads) = YEAR(:dateLeads)")
    Long countCustomersByYear(@Param("dateLeads") Date dateLeads);

    @Query("SELECT c FROM Customer c WHERE MONTH(c.dateLeads) = :month AND YEAR(c.dateLeads) = :year")
    List<Customer> findCustomersByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE YEAR(c.dateLeads) = :year")
    Long countCustomersByYear(@Param("year") int year);

    @Query("SELECT c FROM Customer c WHERE c.dateLeads = :today")
    List<Customer> findCustomersByToday(@Param("today") Date today);


	@Query("select o from Customer o where o.active = 1")
	List<Customer> getfindAll();
	
	Customer getByEmail(String email); 
	Customer getFindById(Integer id);

}
