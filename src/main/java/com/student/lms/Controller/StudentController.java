package com.student.lms.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.lms.Model.Student;
import com.student.lms.Service.StudentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/students")
@CrossOrigin("*")
@Slf4j
public class StudentController {
	
	  @Autowired
	    private StudentService studentService;

	    // CREATE
	    @PostMapping
	    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
	        Student created = studentService.createStudent(student);
	        return ResponseEntity.ok(created);
	    }

	    // READ - Get all
	    @GetMapping
	    public ResponseEntity<List<Student>> getAllStudents() {
	        return ResponseEntity.ok(studentService.getAllStudents());
	    }

	    // READ - Get by ID
	    @GetMapping("/{id}")
	    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
	        Student student = studentService.getStudentById(id);
	        return ResponseEntity.ok(student);
	    }

	    // READ - Get by Email
	    @GetMapping("/email/{email}")
	    public ResponseEntity<Student> getStudentByEmail(@PathVariable String email) {
	        return ResponseEntity.ok(studentService.findByEmail(email));
	    }

	    // UPDATE
	    @PutMapping("/{id}")
	    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updated) {
	        Student student = studentService.updateStudent(id, updated);
	        return ResponseEntity.ok(student);
	    }

	    // DELETE
	    @DeleteMapping("/{id}")
	    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
	        studentService.deleteStudent(id);
	        return ResponseEntity.ok("Student deleted successfully: " + id);
	    }
	    
	    @GetMapping("/csrf-token")
	    public CsrfToken GetCsrfToken(HttpServletRequest request) {
	    	return (CsrfToken) request.getAttribute("_csrf");
	    }
	    
	    @PostMapping("/login")
	    public String login(@RequestBody Student user) {
	    	System.out.println(user);
	    return studentService.verify(user)	;
	    }

}
