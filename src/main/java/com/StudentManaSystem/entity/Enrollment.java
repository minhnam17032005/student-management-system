package com.StudentManaSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(// tránh student đăng ký 1 khóa học 2 lần
		name = "enrollment", uniqueConstraints = @UniqueConstraint(columnNames = { "student_id", "course_id" }))
public class Enrollment extends BaseEntity {

	//Quan hệ 1 Enrollment - nhiều Student.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	//Quan hệ 1 Enrollment - nhiều Course.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	public Enrollment() {
	}

	// Constructor chỉ nhận student và course
	public Enrollment(Student student, Course course) {
		this.student = student;
		this.course = course;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
