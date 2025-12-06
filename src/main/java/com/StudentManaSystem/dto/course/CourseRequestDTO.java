package com.StudentManaSystem.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CourseRequestDTO {

	@NotBlank(message = "tên khóa học không được trống")
	@Size(min = 3, max = 100, message = "tên khóa học giới hạn từ 3 đến 100 ký tự")
	private String name;

	@Size(max = 500, message = "mô tả khóa học giới hạn dưới 500 kú tự")
	private String description;

	@NotNull(message = "Phải chọn instructor")
	private Long instructor_id;

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
