package com.StudentManaSystem.Service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.StudentManaSystem.Service.EnrollmentService;
import com.StudentManaSystem.dto.enrollment.EnrollmentRequestDTO;
import com.StudentManaSystem.dto.enrollment.EnrollmentResponseDTO;
import com.StudentManaSystem.entity.Course;
import com.StudentManaSystem.entity.Enrollment;
import com.StudentManaSystem.entity.Student;
import com.StudentManaSystem.repository.CourseRepository;
import com.StudentManaSystem.repository.EnrollmentRepository;
import com.StudentManaSystem.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

	private final EnrollmentRepository enrollmentRepository;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;

	public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository,
			CourseRepository courseRepository) {
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
		this.enrollmentRepository = enrollmentRepository;
	}

	// lấy tất cả tuyển sinh
	public List<EnrollmentResponseDTO> getAllEnrollments() {
		List<Enrollment> enrollments = enrollmentRepository.findAll();
		List<EnrollmentResponseDTO> listEnrollmentResponse = new ArrayList<>();
		enrollments.forEach(enrollment -> {
			EnrollmentResponseDTO dto = new EnrollmentResponseDTO();
			dto.setId(enrollment.getId());
			dto.setStudent_id(enrollment.getStudent().getId());
			dto.setCourse_id(enrollment.getCourse().getId());

			listEnrollmentResponse.add(dto);
		});
		return listEnrollmentResponse;
	}

	// lấy tuyển sinh theo id
	public EnrollmentResponseDTO getEnrollmentById(Long id) {
		Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment with id " + id + " not found"));

		EnrollmentResponseDTO enrollmentResponse = new EnrollmentResponseDTO();
		enrollmentResponse.setId(enrollment.getId());
		enrollmentResponse.setStudent_id(enrollment.getStudent().getId());
		enrollmentResponse.setCourse_id(enrollment.getCourse().getId());

		return enrollmentResponse;
	}

	// thêm tuyển sinh mới
	public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO input) {
		if (enrollmentRepository.existsByStudentIdAndCourseId(input.getStudent_id(), input.getCourse_id())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student đã đăng ký khóa học này rồi");
		}

		Student student = studentRepository.findById(input.getStudent_id())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student không tồn tại"));

		Course course = courseRepository.findById(input.getCourse_id())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course không tồn tại"));

		Enrollment newEnrollment = new Enrollment();
		newEnrollment.setStudent(student);
		newEnrollment.setCourse(course);

		Enrollment createdEnrollment = enrollmentRepository.save(newEnrollment);
		EnrollmentResponseDTO output = new EnrollmentResponseDTO();
		output.setId(createdEnrollment.getId());
		output.setStudent_id(createdEnrollment.getStudent().getId());
		output.setCourse_id(createdEnrollment.getCourse().getId());

		return output;
	}

	// cập nhật tuyển sinh

	public EnrollmentResponseDTO updateEnrollment(Long id, EnrollmentRequestDTO input) {
		// 1. Tìm enrollment hiện tại
		Enrollment enrollment = enrollmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment không tồn tại"));

		// 2. Tìm student mới
		Student student = studentRepository.findById(input.getStudent_id())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student không tồn tại"));

		// 3. Tìm course mới
		Course course = courseRepository.findById(input.getCourse_id())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course không tồn tại"));

		// 4. Check trùng UNIQUE (student_id + course_id), trừ record đang update
		if (enrollmentRepository.existsByStudentIdAndCourseIdAndIdNot(input.getStudent_id(), input.getCourse_id(),
				id)) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student đã đăng ký khóa học này");
		}

		enrollment.setStudent(student);
		enrollment.setCourse(course);
		Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

		EnrollmentResponseDTO output = new EnrollmentResponseDTO();
		output.setId(updatedEnrollment.getId());
		output.setStudent_id(updatedEnrollment.getStudent().getId());
		output.setCourse_id(updatedEnrollment.getCourse().getId());

		return output;
	}

	// xóa 1 tuyển sinh
	public void deleteEnrollment(Long id) {
		enrollmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment không tồn tại"));
		enrollmentRepository.deleteById(id);
	}

}
