package com.StudentManaSystem.Service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.StudentManaSystem.Service.StudentService;
import com.StudentManaSystem.dto.student.StudentRequestDTO;
import com.StudentManaSystem.dto.student.StudentResponseDTO;
import com.StudentManaSystem.entity.Student;
import com.StudentManaSystem.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
	// DI StudentRepository
	private final StudentRepository studentRepository;

	public StudentServiceImpl(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	// Lấy tất cả học sinh và convert sang DTO để trả về API
	public List<StudentResponseDTO> getAllStudents() {

		// Lấy toàn bộ dữ liệu từ database
		List<Student> students = studentRepository.findAll();
		List<StudentResponseDTO> listStudentResponse = new ArrayList<>();

		// Mapping từ Entity -> Response DTO
		students.forEach(student -> {
			StudentResponseDTO dto = new StudentResponseDTO();
			dto.setId(student.getId());
			dto.setName(student.getName());
			dto.setEmail(student.getEmail());
			dto.setAge(student.getAge());

			listStudentResponse.add(dto);
		});

		return listStudentResponse;
	}


	// Lấy 1 học sinh theo id, nếu không tồn tại thì trả 404
	public StudentResponseDTO getStudentById(Long id) {

		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, 
						"Student with id " + id + " not found"));

		// Mapping Entity -> DTO
		StudentResponseDTO studentResponse = new StudentResponseDTO();
		studentResponse.setId(student.getId());
		studentResponse.setName(student.getName());
		studentResponse.setEmail(student.getEmail());
		studentResponse.setAge(student.getAge());

		return studentResponse;
	}


	// Tạo học sinh mới, kiểm tra email không được trùng
	public StudentResponseDTO createStudent(StudentRequestDTO input) {

		// Validate email unique
		if (studentRepository.existsByEmail(input.getEmail())) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, 
					"Email đã tồn tại");
		}

		// Mapping Request DTO -> Entity
		Student newStudent = new Student();
		newStudent.setName(input.getName());
		newStudent.setEmail(input.getEmail());
		newStudent.setAge(input.getAge());

		// Lưu xuống database
		Student createdStudent = studentRepository.save(newStudent);

		// Mapping Entity -> Response DTO
		StudentResponseDTO output = new StudentResponseDTO();
		output.setId(createdStudent.getId());
		output.setName(createdStudent.getName());
		output.setEmail(createdStudent.getEmail());
		output.setAge(createdStudent.getAge());

		return output;
	}


	// Cập nhật thông tin học sinh theo id
	public StudentResponseDTO updateStudent(Long id, StudentRequestDTO input) {

		// Kiểm tra student tồn tại
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, 
						"Student không tồn tại"));

		// Kiểm tra email không trùng với student khác
		if (studentRepository.existsByEmailAndIdNot(input.getEmail(), id)) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, 
					"Email đã tồn tại");
		}

		// Cập nhật dữ liệu
		student.setName(input.getName());
		student.setEmail(input.getEmail());
		student.setAge(input.getAge());

		Student updatedStudent = studentRepository.save(student);

		// Mapping Entity -> Response DTO
		StudentResponseDTO output = new StudentResponseDTO();
		output.setId(updatedStudent.getId());
		output.setName(updatedStudent.getName());
		output.setEmail(updatedStudent.getEmail());
		output.setAge(updatedStudent.getAge());

		return output;
	}


	// Xóa học sinh nếu không còn Enrollment liên quan
	public void deleteStudent(Long studentId) {

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, 
						"Student không tồn tại"));

		// Không cho xóa nếu còn quan hệ Enrollment
		if (!student.getEnrollments().isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, 
					"Không thể xóa Student còn Enrollment liên quan");
		}

		studentRepository.delete(student);
	}

}
