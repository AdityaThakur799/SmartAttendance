package com.smartattendance.repository;

import com.smartattendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // 🔍 Search by name or roll no
    List<Student> findByNameContainingIgnoreCaseOrRollNoContainingIgnoreCase(
            String name,
            String rollNo
    );
}
