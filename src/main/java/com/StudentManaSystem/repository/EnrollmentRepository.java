package com.StudentManaSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StudentManaSystem.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	// check "student đã đăng ký course chưa".
	boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

	// existsBy... → kiểm tra xem có tồn tại bản ghi nào không.
	// StudentIdAndCourseId → điều kiện trùng cặp student–course.
	// AndIdNot(id) → loại trừ bản ghi đang update (chính nó).
	boolean existsByStudentIdAndCourseIdAndIdNot(Long studentId, Long courseId, Long id);

}