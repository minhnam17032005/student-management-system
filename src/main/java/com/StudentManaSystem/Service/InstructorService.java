package com.StudentManaSystem.Service;

import java.util.List;

import com.StudentManaSystem.dto.instructor.InstructorRequestDTO;
import com.StudentManaSystem.dto.instructor.InstructorResponseDTO;

public interface InstructorService {
	List<InstructorResponseDTO> getAllInstructors();

	InstructorResponseDTO getInstructorById(Long id);

	InstructorResponseDTO createInstructor(InstructorRequestDTO input);

	InstructorResponseDTO updateInstructor(Long id, InstructorRequestDTO input);

	void deleteInstructor(Long id);
}
