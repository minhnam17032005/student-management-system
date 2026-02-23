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

	// Lấy tất cả khóa học và convert sang DTO để trả về API
	public List<CourseResponseDTO> getAllCourses() {

		// Lấy toàn bộ Course từ database
		List<Course> courses = courseRepository.findAll();
		List<CourseResponseDTO> listStudentResponse = new ArrayList<>();

		// Mapping Entity -> Response DTO
		courses.forEach(course -> {
			CourseResponseDTO dto = new CourseResponseDTO();
			dto.setId(course.getId());
			dto.setName(course.getName());
			dto.setDescription(course.getDescription());

			// Nếu Instructor tồn tại thì lấy id, tránh NullPointerException
			dto.setInstructor_id(
					course.getInstructor() != null 
					? course.getInstructor().getId() 
					: null
			);

			listStudentResponse.add(dto);
		});

		return listStudentResponse;
	}


	// Lấy khóa học theo id, nếu không tồn tại thì trả 404
	public CourseResponseDTO getCourseById(Long id) {

		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, 
						"Course with id " + id + " not found"));

		// Mapping Entity -> DTO
		CourseResponseDTO courseResponse = new CourseResponseDTO();
		courseResponse.setId(course.getId());
		courseResponse.setName(course.getName());
		courseResponse.setDescription(course.getDescription());

		// Check null Instructor
		courseResponse.setInstructor_id(
				course.getInstructor() != null 
				? course.getInstructor().getId() 
				: null
		);

		return courseResponse;
	}


	// Tạo khóa học mới, kiểm tra Instructor tồn tại
	public CourseResponseDTO createCourse(CourseRequestDTO input) {

		// Validate Instructor tồn tại
		Instructor instructor = instructorRepository.findById(input.getInstructor_id())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.BAD_REQUEST, 
						"Instructor không tồn tại"));

		// Mapping Request DTO -> Entity
		Course newCourse = new Course();
		newCourse.setName(input.getName());
		newCourse.setDescription(input.getDescription());
		newCourse.setInstructor(instructor);

		// Lưu xuống database
		Course createdCourse = courseRepository.save(newCourse);

		// Mapping Entity -> Response DTO
		CourseResponseDTO output = new CourseResponseDTO();
		output.setId(createdCourse.getId());
		output.setName(createdCourse.getName());
		output.setDescription(createdCourse.getDescription());
		output.setInstructor_id(createdCourse.getInstructor().getId());

		return output;
	}


	// Cập nhật khóa học theo id
	public CourseResponseDTO updateCourse(Long id, CourseRequestDTO input) {

		// 1. Kiểm tra Course tồn tại
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, 
						"Course không tồn tại"));

		// 2. Kiểm tra Instructor tồn tại
		Instructor instructor = instructorRepository.findById(input.getInstructor_id())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.BAD_REQUEST, 
						"Instructor không tồn tại"));

		// Cập nhật dữ liệu
		course.setName(input.getName());
		course.setDescription(input.getDescription());
		course.setInstructor(instructor);

		Course updatedCourse = courseRepository.save(course);

		// Mapping Entity -> DTO
		CourseResponseDTO output = new CourseResponseDTO();
		output.setId(updatedCourse.getId());
		output.setName(updatedCourse.getName());
		output.setDescription(updatedCourse.getDescription());
		output.setInstructor_id(updatedCourse.getInstructor().getId());

		return output;
	}


	// Xóa khóa học theo id
	public void deleteCourse(Long id) {

		// Kiểm tra Course tồn tại trước khi xóa
		courseRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, 
						"Course không tồn tại"));

		courseRepository.deleteById(id);
	}

}
