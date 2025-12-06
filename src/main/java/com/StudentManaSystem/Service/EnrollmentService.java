package com.StudentManaSystem.Service;

import java.util.List;

import com.StudentManaSystem.dto.enrollment.EnrollmentRequestDTO;
import com.StudentManaSystem.dto.enrollment.EnrollmentResponseDTO;

public interface EnrollmentService {
	List<EnrollmentResponseDTO> getAllEnrollments();

	EnrollmentResponseDTO getEnrollmentById(Long id);

	EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO input);

	EnrollmentResponseDTO updateEnrollment(Long id, EnrollmentRequestDTO input);

	void deleteEnrollment(Long id);
}
