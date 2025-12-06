package com.StudentManaSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.StudentManaSystem.Service.CourseService;
import com.StudentManaSystem.dto.course.CourseRequestDTO;
import com.StudentManaSystem.dto.course.CourseResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
public class CourseController {
	// DI
	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	// Lấy tất cả khóa học
	@GetMapping
	public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
		List<CourseResponseDTO> list = courseService.getAllCourses();
		return ResponseEntity.ok(list);
	}

	// Lấy khóa học theo id
	@GetMapping("/{id}")
	public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
		CourseResponseDTO course = courseService.getCourseById(id);
		return ResponseEntity.ok(course);
	}

	// Thêm khóa học mới
	@PostMapping
	public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO input) {
		CourseResponseDTO created = courseService.createCourse(input);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// Cập nhật khóa học
	@PutMapping("/{id}")
	public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id,
			@Valid @RequestBody CourseRequestDTO input) {
		CourseResponseDTO updated = courseService.updateCourse(id, input);
		return ResponseEntity.ok(updated);
	}

	// Xóa khóa học
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
		courseService.deleteCourse(id);
		return ResponseEntity.noContent().build(); // 204 No Content
	}

}
