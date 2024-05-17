package com.f_crm.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "ExpertDoctors")
public class ExpertDoctor implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@NotBlank(message = "Tên chuyên ngành không được bỏ trống")
	@Nationalized
	String name;

	boolean active = true;

	@JsonIgnore
	@OneToMany(mappedBy = "expertdoctor")
	List<Users> users;

	@JsonIgnore
	@OneToMany(mappedBy = "expertdoctors")
	List<Crm_entry> crm_entries;
}
