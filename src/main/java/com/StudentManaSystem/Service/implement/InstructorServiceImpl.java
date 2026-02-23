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

	// Lấy tất cả Instructor và convert sang DTO để trả về API
	public List<InstructorResponseDTO> getAllInstructors() {

		// Lấy toàn bộ dữ liệu từ database
		List<Instructor> instructors = instructorRepository.findAll();
		List<InstructorResponseDTO> listInstructorResponse = new ArrayList<>();

		// Mapping Entity -> Response DTO
		instructors.forEach(instructor -> {
			InstructorResponseDTO dto = new InstructorResponseDTO();
			dto.setId(instructor.getId());
			dto.setName(instructor.getName());
			dto.setEmail(instructor.getEmail());

			listInstructorResponse.add(dto);
		});

		return listInstructorResponse;
	}


	// Lấy Instructor theo id, nếu không tồn tại trả 404
	public InstructorResponseDTO getInstructorById(Long id) {

		Instructor instructor = instructorRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Instructor with id " + id + " not found"));

		// Mapping Entity -> DTO
		InstructorResponseDTO instructorResponse = new InstructorResponseDTO();
		instructorResponse.setId(instructor.getId());
		instructorResponse.setName(instructor.getName());
		instructorResponse.setEmail(instructor.getEmail());

		return instructorResponse;
	}


	// Tạo Instructor mới, kiểm tra email không được trùng
	public InstructorResponseDTO createInstructor(InstructorRequestDTO input) {

		// Validate email unique
		if (instructorRepository.existsByEmail(input.getEmail())) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Email đã tồn tại");
		}

		// Mapping Request DTO -> Entity
		Instructor newInstructor = new Instructor();
		newInstructor.setName(input.getName());
		newInstructor.setEmail(input.getEmail());

		// Lưu xuống database
		Instructor createdInstructor = instructorRepository.save(newInstructor);

		// Mapping Entity -> DTO
		InstructorResponseDTO output = new InstructorResponseDTO();
		output.setId(createdInstructor.getId());
		output.setName(createdInstructor.getName());
		output.setEmail(createdInstructor.getEmail());

		return output;
	}


	// Cập nhật Instructor theo id
	public InstructorResponseDTO updateInstructor(Long id, InstructorRequestDTO input) {

		// Kiểm tra Instructor tồn tại
		Instructor instructor = instructorRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Instructor không tồn tại"));

		// Kiểm tra email không trùng với Instructor khác
		if (instructorRepository.existsByEmailAndIdNot(input.getEmail(), id)) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Email đã tồn tại");
		}

		// Cập nhật dữ liệu
		instructor.setName(input.getName());
		instructor.setEmail(input.getEmail());

		Instructor updatedInstructor = instructorRepository.save(instructor);

		// Mapping Entity -> DTO
		InstructorResponseDTO output = new InstructorResponseDTO();
		output.setId(updatedInstructor.getId());
		output.setName(updatedInstructor.getName());
		output.setEmail(updatedInstructor.getEmail());

		return output;
	}


	/* Xóa Instructor:
	* Vì Course có khóa ngoại instructor_id,
	* nên phải set instructor = null cho các Course liên quan
	* trước khi xóa để tránh lỗi ràng buộc khóa ngoại.*/
	public void deleteInstructor(Long id) {

		// Kiểm tra Instructor tồn tại
		Instructor instructor = instructorRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND,
						"Instructor không tồn tại"));

		// Lấy tất cả Course có instructor_id = id
		List<Course> courses = courseRepository.findByInstructorId(id);

		// Set instructor = null để tránh lỗi foreign key
		for (Course c : courses) {
			c.setInstructor(null);
		}

		// Lưu thay đổi xuống database
		courseRepository.saveAll(courses);

		// Flush để đảm bảo cập nhật hoàn tất trước khi xóa
		courseRepository.flush();

		// Xóa Instructor
		instructorRepository.delete(instructor);
	}
}
