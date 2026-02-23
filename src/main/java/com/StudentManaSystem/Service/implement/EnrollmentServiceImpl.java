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

	// Lấy tất cả Enrollment và convert sang DTO để trả về API
	public List<EnrollmentResponseDTO> getAllEnrollments() {

		// Lấy toàn bộ dữ liệu từ database
		List<Enrollment> enrollments = enrollmentRepository.findAll();
		List<EnrollmentResponseDTO> listEnrollmentResponse = new ArrayList<>();

		// Mapping từ Entity -> Response DTO
		enrollments.forEach(enrollment -> {
			EnrollmentResponseDTO dto = new EnrollmentResponseDTO();
			dto.setId(enrollment.getId());
			dto.setStudent_id(enrollment.getStudent().getId());
			dto.setCourse_id(enrollment.getCourse().getId());

			listEnrollmentResponse.add(dto);
		});

		return listEnrollmentResponse;
	}


	// Lấy 1 Enrollment theo id, nếu không tồn tại thì trả 404
	public EnrollmentResponseDTO getEnrollmentById(Long id) {

		Enrollment enrollment = enrollmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Enrollment with id " + id + " not found"));

		// Mapping Entity -> DTO
		EnrollmentResponseDTO enrollmentResponse = new EnrollmentResponseDTO();
		enrollmentResponse.setId(enrollment.getId());
		enrollmentResponse.setStudent_id(enrollment.getStudent().getId());
		enrollmentResponse.setCourse_id(enrollment.getCourse().getId());

		return enrollmentResponse;
	}


	// Tạo Enrollment mới, kiểm tra không được trùng (student_id + course_id)
	public EnrollmentResponseDTO createEnrollment(EnrollmentRequestDTO input) {

		// Validate unique (student_id + course_id)
		if (enrollmentRepository.existsByStudentIdAndCourseId(
				input.getStudent_id(), input.getCourse_id())) {

			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Student đã đăng ký khóa học này rồi");
		}

		// Kiểm tra student tồn tại
		Student student = studentRepository.findById(input.getStudent_id())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Student không tồn tại"));

		// Kiểm tra course tồn tại
		Course course = courseRepository.findById(input.getCourse_id())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Course không tồn tại"));

		// Mapping Request DTO -> Entity
		Enrollment newEnrollment = new Enrollment();
		newEnrollment.setStudent(student);
		newEnrollment.setCourse(course);

		// Lưu xuống database
		Enrollment createdEnrollment = enrollmentRepository.save(newEnrollment);

		// Mapping Entity -> Response DTO
		EnrollmentResponseDTO output = new EnrollmentResponseDTO();
		output.setId(createdEnrollment.getId());
		output.setStudent_id(createdEnrollment.getStudent().getId());
		output.setCourse_id(createdEnrollment.getCourse().getId());

		return output;
	}


	// Cập nhật Enrollment theo id
	public EnrollmentResponseDTO updateEnrollment(Long id, EnrollmentRequestDTO input) {

		// 1. Kiểm tra enrollment tồn tại
		Enrollment enrollment = enrollmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Enrollment không tồn tại"));

		// 2. Kiểm tra student mới tồn tại
		Student student = studentRepository.findById(input.getStudent_id())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Student không tồn tại"));

		// 3. Kiểm tra course mới tồn tại
		Course course = courseRepository.findById(input.getCourse_id())
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.BAD_REQUEST,
						"Course không tồn tại"));

		// 4. Kiểm tra không trùng (student_id + course_id), loại trừ chính record đang update
		if (enrollmentRepository.existsByStudentIdAndCourseIdAndIdNot(
				input.getStudent_id(),
				input.getCourse_id(),
				id)) {

			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Student đã đăng ký khóa học này");
		}

		// Cập nhật dữ liệu
		enrollment.setStudent(student);
		enrollment.setCourse(course);

		Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

		// Mapping Entity -> Response DTO
		EnrollmentResponseDTO output = new EnrollmentResponseDTO();
		output.setId(updatedEnrollment.getId());
		output.setStudent_id(updatedEnrollment.getStudent().getId());
		output.setCourse_id(updatedEnrollment.getCourse().getId());

		return output;
	}


	// Xóa Enrollment theo id, nếu không tồn tại thì trả 404
	public void deleteEnrollment(Long id) {

		Enrollment enrollment = enrollmentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Enrollment không tồn tại"));

		enrollmentRepository.delete(enrollment);
	}
}
