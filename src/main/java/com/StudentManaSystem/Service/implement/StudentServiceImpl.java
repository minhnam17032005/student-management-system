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

	// lấy tất cả học sinh
	public List<StudentResponseDTO> getAllStudents() {
		// thêm tất cả học sinh vào students
		List<Student> students = studentRepository.findAll();
		List<StudentResponseDTO> listStudentResponse = new ArrayList<>();

		// thêm lần lượt các object của students sang dto(StudentResponseDTO)
		students.forEach(student -> {
			StudentResponseDTO dto = new StudentResponseDTO();
			dto.setId(student.getId());
			dto.setName(student.getName());
			dto.setEmail(student.getEmail());
			dto.setAge(student.getAge());

			// thêm từng giá trị vào listStudentResponse
			listStudentResponse.add(dto);
		});

		return listStudentResponse;
	}

	// lấy học sinh theo id
	public StudentResponseDTO getStudentById(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id " + id + " not found"));

		StudentResponseDTO studentResponse = new StudentResponseDTO();
		studentResponse.setId(student.getId());
		studentResponse.setName(student.getName());
		studentResponse.setEmail(student.getEmail());
		studentResponse.setAge(student.getAge());

		return studentResponse;
	};

	// thêm 1 học sinh mới
	public StudentResponseDTO createStudent(StudentRequestDTO input) {
		if (studentRepository.existsByEmail(input.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");
		}

		Student newStudent = new Student();
		newStudent.setName(input.getName());
		newStudent.setEmail(input.getEmail());
		newStudent.setAge(input.getAge());

		Student createdStudent = studentRepository.save(newStudent);

		StudentResponseDTO output = new StudentResponseDTO();
		output.setId(createdStudent.getId());
		output.setName(createdStudent.getName());
		output.setEmail(createdStudent.getEmail());
		output.setAge(createdStudent.getAge());

		return output;
	};

	// cập nhật học sinh
	public StudentResponseDTO updateStudent(Long id, StudentRequestDTO input) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student không tồn tại"));

		if (studentRepository.existsByEmailAndIdNot(input.getEmail(), id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");
		}
		student.setName(input.getName());
		student.setEmail(input.getEmail());
		student.setAge(input.getAge());

		Student updatedStudent = studentRepository.save(student);

		StudentResponseDTO output = new StudentResponseDTO();
		output.setId(updatedStudent.getId());
		output.setName(updatedStudent.getName());
		output.setEmail(updatedStudent.getEmail());
		output.setAge(updatedStudent.getAge());

		return output;
	};

	public void deleteStudent(Long studentId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student không tồn tại"));

		if (!student.getEnrollments().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa Student còn Enrollment liên quan");
		}
		studentRepository.delete(student);
	}

}
