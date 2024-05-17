package com.f_crm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Customers")
public class Customer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@NotBlank(message = "Họ và tên không được để trống!")
	@Nationalized
	String fullname;

	@NotBlank(message = "Địa chỉ không được để trống!")
	@Nationalized
	String address;

	@NotBlank(message = "Số điện thoại không được để trống!")
	@Size(max = 10, min = 10, message = "Số điện thoại phải 10 số!")
	@Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})", message = "Sai định dạng số điện thoại!")
	String phone;

	@NotBlank(message = "Email không được để trống!")
	@Email(message = "Sai định dạng email!")
	String email;

	@NotNull(message = "Giới tính không được để trống!")
	boolean gender;

	@Nationalized
	@NotNull(message = "Phương thức đến không được để trống!")
	String arrival_method;

	@Temporal(TemporalType.DATE)
	@Column(name = "Birthday")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date birthday;

	@Temporal(TemporalType.DATE)
	@Column(name = "DateLeads")
	Date dateLeads = new Date();

	boolean doctor_appointment;

	@Temporal(TemporalType.DATE)
	@Column(name = "Appointment_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date appointment_date;

	boolean hospital_examination;

	@Temporal(TemporalType.DATE)
	@Column(name = "Treatment_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date treatment_date;

	boolean surgery;

	@Temporal(TemporalType.DATE)
	@Column(name = "Surgery_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date surgery_date;

	@Nationalized
	String note;

	boolean active = true;

	boolean voucher;

	@ManyToOne
	@JoinColumn(name = "Username")
	Users user;

	@ManyToOne
	@JoinColumn(name = "methodid")
	Method methods;

	@JsonIgnore
	@OneToMany(mappedBy = "customers")
	List<Crm_entry> crm_entries;

	public void setTreatmentDate(Date treatmentDate) {
		this.treatment_date = treatmentDate;
	}
}
