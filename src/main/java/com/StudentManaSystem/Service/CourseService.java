package com.StudentManaSystem.Service;

import java.util.List;

import com.StudentManaSystem.dto.course.CourseRequestDTO;
import com.StudentManaSystem.dto.course.CourseResponseDTO;

public interface CourseService {
	List<CourseResponseDTO> getAllCourses();

	CourseResponseDTO getCourseById(Long id);

	CourseResponseDTO createCourse(CourseRequestDTO input);

	CourseResponseDTO updateCourse(Long id, CourseRequestDTO input);

	void deleteCourse(Long id);
}
