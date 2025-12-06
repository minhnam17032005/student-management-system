package com.StudentManaSystem.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "instructor")
public class Instructor extends BaseEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@OneToMany(mappedBy = "instructor")
	private List<Course> courses = new ArrayList<>();

	public Instructor() {
	}

	public Instructor(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
