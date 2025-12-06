package com.StudentManaSystem.dto.instructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InstructorRequestDTO {
	@NotNull
	@NotBlank(message = "tên không được trống")
	@Size(min = 2, max = 50, message = "tên giới hạn từ 2 đến 50 ký tự")
	private String name;

	@NotNull
	@NotBlank(message = "email không được rỗng")
	@Email(message = "email sai định dạng")
	@Size(max = 100, message = "email giới hạn dưới 100 ký tự")
	private String email;

	public InstructorRequestDTO() {
	}

	public InstructorRequestDTO(String name, String email) {
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
