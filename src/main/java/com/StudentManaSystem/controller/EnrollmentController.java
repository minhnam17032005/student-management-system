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

import com.StudentManaSystem.Service.EnrollmentService;
import com.StudentManaSystem.dto.enrollment.EnrollmentRequestDTO;
import com.StudentManaSystem.dto.enrollment.EnrollmentResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
	// DI
	private final EnrollmentService enrollmentService;

	public EnrollmentController(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	// Lấy tất cả tuyển sinh
	@GetMapping
	public ResponseEntity<List<EnrollmentResponseDTO>> getAllEnrollments() {
		List<EnrollmentResponseDTO> list = enrollmentService.getAllEnrollments();
		return ResponseEntity.ok(list);
	}

	// Lấy tuyển sinh theo id
	@GetMapping("/{id}")
	public ResponseEntity<EnrollmentResponseDTO> getEnrollmentById(@PathVariable Long id) {
		EnrollmentResponseDTO enrollment = enrollmentService.getEnrollmentById(id);
		return ResponseEntity.ok(enrollment);
	}

	// Thêm tuyển sinh mới
	@PostMapping
	public ResponseEntity<EnrollmentResponseDTO> createEnrollment(@Valid @RequestBody EnrollmentRequestDTO input) {
		EnrollmentResponseDTO created = enrollmentService.createEnrollment(input);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// Cập nhật tuyển sinh
	@PutMapping("/{id}")
	public ResponseEntity<EnrollmentResponseDTO> updateEnrollment(@PathVariable Long id,
			@Valid @RequestBody EnrollmentRequestDTO input) {
		EnrollmentResponseDTO updated = enrollmentService.updateEnrollment(id, input);
		return ResponseEntity.ok(updated);
	}

	// Xóa tuyển sinh
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
		enrollmentService.deleteEnrollment(id);
		return ResponseEntity.noContent().build(); // 204 No Content
	}

}
