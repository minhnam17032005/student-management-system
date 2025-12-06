package com.StudentManaSystem.dto.course;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "name", "description", "instructor_id" })
public class CourseResponseDTO {

	private Long id;

	private String name;

	private String description;

	private Long instructor_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getInstructor_id() {
		return instructor_id;
	}

	public void setInstructor_id(Long instructor_id) {
		this.instructor_id = instructor_id;
	}

}
