package com.f_crm.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.Parent;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Users")
public class Users implements Serializable {

	@Id
	@NotBlank(message = "Tên đăng nhập không được để trống!")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Tên đăng nhập không được chứa ký tự đặc biệt!")
	String username;

	@NotBlank(message = "Mật khẩu không được để trống!")
	@Size(min = 8, message = "Mật khẩu phải ít nhất 8 ký tự!")
	String password;

	@Nationalized
	@NotBlank(message = "Họ và tên không được để trống!")
	String fullname;

	@Nationalized
	@NotBlank(message = "Địa chỉ không được để trống!")
	String address;

	@NotBlank(message = "Số điện thoại không được để trống!")
	@Size(max = 10, min = 10, message = "Số điện thoại phải 10 số!")
	@Pattern(regexp = "^(0[2|3|5|7|8|9])+([0-9]{8})", message = "Sai định dạng số điện thoại!")
	String phone;

	@NotBlank(message = "Email không được để trống!")
	@Email(message = "Sai định dạng email!")
	String email;

	String photo;

	@NotNull(message = "Giới tính không được để trống!")
	boolean gender;

	boolean active = true;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER) // fetch = ... tải dữ liệu
	List<Authority> authorities;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Customer> customers;

	@ManyToOne
	@JoinColumn(name = "expertDoctorid")
	@NotNull(message = "Chuyên ngành không được để trống!")
	private ExpertDoctor expertdoctor;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	List<Crm_entry> crm_entries;
}
