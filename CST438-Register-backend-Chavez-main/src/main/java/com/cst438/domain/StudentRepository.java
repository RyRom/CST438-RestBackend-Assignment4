package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends CrudRepository <Student, Integer> {
	 @Query("Select s from Student s where s.email = :email")
	 Student findByEmail(String email); 
	 @Query("Select s from Student s where s.name like :name%")
	 Student[] findByNameStartsWith(String name);
	 @Query("Select s from Student s where s.student_id= :student_id")
	 Student findByStudent_id(int student_id);
}
