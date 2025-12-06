package com.StudentManaSystem.Service;

import java.util.List;

import com.StudentManaSystem.dto.student.StudentRequestDTO;
import com.StudentManaSystem.dto.student.StudentResponseDTO;

public interface StudentService {
	List<StudentResponseDTO> getAllStudents();

	StudentResponseDTO getStudentById(Long id);

	StudentResponseDTO createStudent(StudentRequestDTO input);

	StudentResponseDTO updateStudent(Long id, StudentRequestDTO input);

	void deleteStudent(Long id);
}
