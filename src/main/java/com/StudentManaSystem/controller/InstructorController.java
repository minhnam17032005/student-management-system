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

import com.StudentManaSystem.Service.InstructorService;
import com.StudentManaSystem.dto.instructor.InstructorRequestDTO;
import com.StudentManaSystem.dto.instructor.InstructorResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

	// DI
	private final InstructorService instructorService;

	public InstructorController(InstructorService instructorService) {
		this.instructorService = instructorService;
	}

	// Lấy tất cả người hướng dẫn
	@GetMapping
	public ResponseEntity<List<InstructorResponseDTO>> getAllInstructors() {
		List<InstructorResponseDTO> list = instructorService.getAllInstructors();
		return ResponseEntity.ok(list);
	}

	// Lấy người hướng dẫn theo id
	@GetMapping("/{id}")
	public ResponseEntity<InstructorResponseDTO> getInstructorById(@PathVariable Long id) {
		InstructorResponseDTO instructor = instructorService.getInstructorById(id);
		return ResponseEntity.ok(instructor);
	}

	// Thêm người hướng dẫn mới
	@PostMapping
	public ResponseEntity<InstructorResponseDTO> createInstructor(@Valid @RequestBody InstructorRequestDTO input) {
		InstructorResponseDTO created = instructorService.createInstructor(input);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// Cập nhật người hướng dẫn
	@PutMapping("/{id}")
	public ResponseEntity<InstructorResponseDTO> updateInstructor(@PathVariable Long id,
			@Valid @RequestBody InstructorRequestDTO input) {
		InstructorResponseDTO updated = instructorService.updateInstructor(id, input);
		return ResponseEntity.ok(updated);
	}

	// Xóa người hướng dẫn
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
		instructorService.deleteInstructor(id);
		return ResponseEntity.noContent().build(); // 204 No Content
	}

}
