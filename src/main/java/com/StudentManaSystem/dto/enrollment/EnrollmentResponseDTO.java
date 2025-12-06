package com.StudentManaSystem.dto.enrollment;

public class EnrollmentResponseDTO {

	private Long id;
	private Long student_id;
	private Long course_id;

	public EnrollmentResponseDTO() {
	}

	public EnrollmentResponseDTO(Long id, Long student_id, Long course_id) {
		this.id = id;
		this.student_id = student_id;
		this.course_id = course_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Long student_id) {
		this.student_id = student_id;
	}

	public Long getCourse_id() {
		return course_id;
	}

	public void setCourse_id(Long course_id) {
		this.course_id = course_id;
	}
}
