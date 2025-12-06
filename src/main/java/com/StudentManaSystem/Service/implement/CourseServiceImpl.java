package com.StudentManaSystem.Service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.StudentManaSystem.Service.CourseService;
import com.StudentManaSystem.dto.course.CourseRequestDTO;
import com.StudentManaSystem.dto.course.CourseResponseDTO;
import com.StudentManaSystem.entity.Course;
import com.StudentManaSystem.entity.Instructor;
import com.StudentManaSystem.repository.CourseRepository;
import com.StudentManaSystem.repository.InstructorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
	// DI CourseRepository
	private final CourseRepository courseRepository;
	// DI InstructorRepository
	private final InstructorRepository instructorRepository;

	public CourseServiceImpl(CourseRepository courseRepository, InstructorRepository instructorRepository) {
		this.courseRepository = courseRepository;
		this.instructorRepository = instructorRepository;
	}

	// lấy tất cả khóa học
	public List<CourseResponseDTO> getAllCourses() {
		List<Course> courses = courseRepository.findAll();
		List<CourseResponseDTO> listStudentResponse = new ArrayList<>();
		courses.forEach(course -> {
			CourseResponseDTO dto = new CourseResponseDTO();
			dto.setId(course.getId());
			dto.setName(course.getName());
			dto.setDescription(course.getDescription());
			// check null Instructor
			dto.setInstructor_id(course.getInstructor() != null ? course.getInstructor().getId() : null);

			listStudentResponse.add(dto);
		});
		return listStudentResponse;
	}

	// lấy khóa học theo id
	public CourseResponseDTO getCourseById(Long id) {
		Course course = courseRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with id " + id + " not found"));
		CourseResponseDTO courseResponse = new CourseResponseDTO();
		courseResponse.setId(course.getId());
		courseResponse.setName(course.getName());
		courseResponse.setDescription(course.getDescription());
		// check null Instructor
		courseResponse.setInstructor_id(course.getInstructor() != null ? course.getInstructor().getId() : null);
		return courseResponse;
	}

	// thêm khóa học mới
	public CourseResponseDTO createCourse(CourseRequestDTO input) {
		Instructor instructor = instructorRepository.findById(input.getInstructor_id())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Instructor không tồn tại"));

		Course newCourse = new Course();
		newCourse.setName(input.getName());
		newCourse.setDescription(input.getDescription());
		newCourse.setInstructor(instructor);

		Course createdCourse = courseRepository.save(newCourse);

		CourseResponseDTO output = new CourseResponseDTO();
		output.setId(createdCourse.getId());
		output.setName(createdCourse.getName());
		output.setDescription(createdCourse.getDescription());
		output.setInstructor_id(createdCourse.getInstructor().getId());

		return output;
	}

	// cập nhật khóa học
	public CourseResponseDTO updateCourse(Long id, CourseRequestDTO input) {
		// 1. Kiểm tra course có tồn tại không
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course không tồn tại"));

		// 2. Kiểm tra instructor có tồn tại không
		Instructor instructor = instructorRepository.findById(input.getInstructor_id())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Instructor không tồn tại"));
		course.setName(input.getName());
		course.setDescription(input.getDescription());
		course.setInstructor(instructor);

		Course updatedCourse = courseRepository.save(course);

		CourseResponseDTO output = new CourseResponseDTO();
		output.setId(updatedCourse.getId());
		output.setName(updatedCourse.getName());
		output.setDescription(updatedCourse.getDescription());
		output.setInstructor_id(updatedCourse.getInstructor().getId());

		return output;
	}

	// xóa khóa học
	public void deleteCourse(Long id) {
		courseRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student không tồn tại"));
		courseRepository.deleteById(id);
	}

}
