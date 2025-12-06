package com.StudentManaSystem.Service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.StudentManaSystem.Service.InstructorService;
import com.StudentManaSystem.dto.instructor.InstructorRequestDTO;
import com.StudentManaSystem.dto.instructor.InstructorResponseDTO;
import com.StudentManaSystem.entity.Course;
import com.StudentManaSystem.entity.Instructor;
import com.StudentManaSystem.repository.CourseRepository;
import com.StudentManaSystem.repository.InstructorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class InstructorServiceImpl implements InstructorService {
	// nên sử dụng autowired
	private final InstructorRepository instructorRepository;
	private final CourseRepository courseRepository;

	public InstructorServiceImpl(InstructorRepository instructorRepository, CourseRepository courseRepository) {
		this.instructorRepository = instructorRepository;
		this.courseRepository = courseRepository;
	}

	// lấy tất cả người hướng dẫn
	public List<InstructorResponseDTO> getAllInstructors() {
		List<Instructor> instructors = instructorRepository.findAll();
		List<InstructorResponseDTO> listInstructorResponse = new ArrayList<>();

		instructors.forEach(instructor -> {
			InstructorResponseDTO dto = new InstructorResponseDTO();
			dto.setId(instructor.getId());
			dto.setName(instructor.getName());
			dto.setEmail(instructor.getEmail());

			listInstructorResponse.add(dto);
		});
		return listInstructorResponse;
	}

	// lấy người hướng dẫn theo id
	public InstructorResponseDTO getInstructorById(Long id) {
		Instructor instructor = instructorRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor with id " + id + " not found"));

		InstructorResponseDTO instructorResponse = new InstructorResponseDTO();
		instructorResponse.setId(instructor.getId());
		instructorResponse.setName(instructor.getName());
		instructorResponse.setEmail(instructor.getEmail());

		return instructorResponse;

	}

	// thêm người hướng dẫn mới
	public InstructorResponseDTO createInstructor(InstructorRequestDTO input) {
		if (instructorRepository.existsByEmail(input.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");
		}
		Instructor newInstructor = new Instructor();
		newInstructor.setName(input.getName());
		newInstructor.setEmail(input.getEmail());

		Instructor createdInstructor = instructorRepository.save(newInstructor);

		InstructorResponseDTO output = new InstructorResponseDTO();
		output.setId(createdInstructor.getId());
		output.setName(createdInstructor.getName());
		output.setEmail(createdInstructor.getEmail());

		return output;
	}

	// cập nhật người hướng dẫn
	public InstructorResponseDTO updateInstructor(Long id, InstructorRequestDTO input) {
		Instructor instructor = instructorRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "instructor không tồn tại"));

		if (instructorRepository.existsByEmailAndIdNot(input.getEmail(), id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại");
		}

		instructor.setName(input.getName());
		instructor.setEmail(input.getEmail());

		Instructor updatedInstructor = instructorRepository.save(instructor);
		InstructorResponseDTO output = new InstructorResponseDTO();
		output.setId(updatedInstructor.getId());
		output.setName(updatedInstructor.getName());
		output.setEmail(updatedInstructor.getEmail());

		return output;
	}

	public void deleteInstructor(Long id) {
		Instructor instructor = instructorRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "instructor không tồn tại"));

		// 2. Lấy tất cả course có instructor_id = id
		List<Course> courses = courseRepository.findByInstructorId(id);

		// 3. Set instructor = null cho từng course
		for (Course c : courses) {
			c.setInstructor(null);
		}

		// 4. Lưu lại course
		courseRepository.saveAll(courses);

		// flush changes trước khi xóa instructor
		courseRepository.flush();

		// Xoá instructor
		instructorRepository.delete(instructor);
	}
}
