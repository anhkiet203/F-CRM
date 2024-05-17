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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "Crm_entrys")
public class Crm_entry implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	@JoinColumn(name = "customerid")
	Customer customers;

	@ManyToOne
	@JoinColumn(name = "methodid")
	Method methods;

	@ManyToOne
	@JoinColumn(name = "Username")
	Users user;

	@ManyToOne
	@JoinColumn(name = "expertdoctorid")
	ExpertDoctor expertdoctors;

}
