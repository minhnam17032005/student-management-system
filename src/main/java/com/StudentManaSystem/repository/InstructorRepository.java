package com.StudentManaSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StudentManaSystem.entity.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
	// Kiểm tra email đã tồn tại hay chưa
	boolean existsByEmail(String email);

	// Tìm bản ghi có email trùng nhưng id khác id đang cập nhật
	boolean existsByEmailAndIdNot(String email, Long id);
}
