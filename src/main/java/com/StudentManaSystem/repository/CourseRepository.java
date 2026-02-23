package com.StudentManaSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StudentManaSystem.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
	//Tìm tất cả Course mà Intructor dạy
	List<Course> findByInstructorId(Long instructorId);

}
