package com.student.lms.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.student.lms.Model.Student;
import com.student.lms.Repository.StudentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Repository
@Slf4j
public class StudentService {
	@Autowired
	 private  StudentRepository studentRepository;
	
	@Autowired
	private AuthenticationManager authmanager;
	
	@Autowired
	private JwtService jwtService;
	
	private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
	
	
	  public Student createStudent(Student student) {
		  log.info("Creating new student: {}", student.getEmail());
	        if (studentRepository.existsByEmail(student.getEmail())) {
	        	log.error("Email already registered: {}", student.getEmail());
	            throw new IllegalArgumentException("Email already registered");
	        }
	        student.setPassword(encoder.encode(student.getPassword()));
	        student.setRole("STUDENT");
	        student.setRegisteredDate(LocalDate.now());
	        Student saved = studentRepository.save(student);
	        log.info("Student created successfully: ID = {}", saved.getId());
	        return saved;
	    }
	  
	  public Student updateStudent(Long id, Student updated) {
		  log.info("Updating student with ID: {}", id);
	        Student existing = studentRepository.getReferenceById(id);

	        existing.setFirstName(updated.getFirstName());
	        existing.setLastName(updated.getLastName());
	        existing.setDateOfBirth(updated.getDateOfBirth());
	        existing.setAddress(updated.getAddress());
	        existing.setPhoneNumber(updated.getPhoneNumber());
	        
	        Student saved=studentRepository.save(existing);
	        log.info("Student updated successfully: ID = {}", saved.getId());

	        
	        return saved;
	    }
	  
	  public Student getStudentById(Long id) {
		  log.info("Fetching student with ID: {}", id);
		  return studentRepository.findById(id)
			        .orElseThrow(() -> new RuntimeException("Student not found: " + id));

	    }
	  
	   public void deleteStudent(Long id) {
		    log.warn("Deleting student with ID: {}", id);
	        studentRepository.deleteById(id);
	        log.info("Student deleted successfully: ID = {}", id);
	    }
	   
	   public List<Student> getAllStudents() {
		   log.info("Fetching all students");
	        return studentRepository.findAll();
	    }
	   
	   public Student findByEmail(String email) {
		   log.info("Finding student by email: {}", email);
	        return studentRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("Student not found: " + email));
	    }
	   
	   public String verify(Student user) {
		   Authentication authentication =authmanager.authenticate(new UsernamePasswordAuthenticationToken(user.getFirstName(), user.getPassword()));
	       if(authentication.isAuthenticated())
	    	   return jwtService.generateToken(user.getFirstName());
	       else {
	    	   return "Failure";
	       }
	   
	   }
	  
	  
}
