package com.StudentManaSystem.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StudentRequestDTO {

	@NotBlank(message = "tên không được trống")
	@Size(min = 2, max = 50, message = "tên giới hạn từ 2 đến 50 ký tự")
	private String name;

	@NotBlank(message = "email không được rỗng")
	@Email(message = "email sai định dạng")
	@Size(max = 100, message = "email giới hạn dưới 100 ký tự")
	private String email;

	@Min(value = 10, message = "tuổi phải trên 10")
	@Max(value = 120, message = "tuổi giới hạn dưới 120")
	private Integer age;

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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
