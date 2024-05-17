package com.f_crm.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Methods")
public class Method implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@NotBlank(message = "Tên phương pháp không được bỏ trống")
	@Nationalized

	String name;

	boolean active = true;

	@OneToMany(mappedBy = "methods")
	List<Customer> customers;

	@JsonIgnore
	@OneToMany(mappedBy = "methods")
	List<Crm_entry> crm_entries;
}
