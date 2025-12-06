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

import com.StudentManaSystem.Service.StudentService;
import com.StudentManaSystem.dto.student.StudentRequestDTO;
import com.StudentManaSystem.dto.student.StudentResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	// Lấy tất cả học sinh
	@GetMapping
	public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
		List<StudentResponseDTO> list = studentService.getAllStudents();
		return ResponseEntity.ok(list);
	}

	// Lấy học sinh theo id
	@GetMapping("/{id}")
	public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
		StudentResponseDTO student = studentService.getStudentById(id);
		return ResponseEntity.ok(student);
	}

	// Thêm học sinh mới
	@PostMapping
	public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO input) {
		StudentResponseDTO created = studentService.createStudent(input);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// Cập nhật học sinh
	@PutMapping("/{id}")
	public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id,
			@Valid @RequestBody StudentRequestDTO input) {
		StudentResponseDTO updated = studentService.updateStudent(id, input);
		return ResponseEntity.ok(updated);
	}

	// Xóa học sinh
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		studentService.deleteStudent(id);
		return ResponseEntity.noContent().build(); // 204 No Content
	}
}
