package com.cst438.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@Service
@ConditionalOnProperty(prefix = "gradebook", name = "service", havingValue = "rest")
@RestController
public class GradebookServiceREST implements GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	private static String gradebook_url;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		System.out.println("Start Message "+ student_email +" " + course_id); 
	
		// TODO use RestTemplate to send message to gradebook service
		Student student = studentRepository.findByEmail(student_email);
		// 2. get course object from course repository
		Course course = courseRepository.findById(course_id).orElse(null);

		// 3. create EnrollmentDTO object
		Enrollment e = new Enrollment();
		e.setStudent(student);
		e.setCourse(course);

		EnrollmentDTO enrollmentDTO = createEnrollmentDTO(e);

		// 4. send POST message to gradebook service
		restTemplate.postForObject(gradebook_url, enrollmentDTO, EnrollmentDTO.class);
		System.out.println("End Message "+ student_email +" " + course_id);

	}
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	/*
	 * endpoint for final course grades
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades( @RequestBody FinalGradeDTO[] grades, @PathVariable("course_id") int course_id) {
		System.out.println("Grades received "+grades.length);
		
		//TODO update grades in enrollment records with grades received from gradebook service
	}
	
	private EnrollmentDTO createEnrollmentDTO(Enrollment e) {
		EnrollmentDTO dto = new EnrollmentDTO(
			e.getEnrollment_id(),
			e.getStudent().getEmail(),
			e.getStudent().getName(),
			e.getCourse().getCourse_id()
		);
		return dto;
	}
}
