package com.student.lms.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.student.lms.Model.Student;
import com.student.lms.Model.UserPrincipal;
import com.student.lms.Repository.StudentRepository;


@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private StudentRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String firstName) throws UsernameNotFoundException {
     Student user=repo.findByFirstName(firstName);
		
     if(user==null) {
    	 System.out.println("User not Found");
    	 throw new UsernameNotFoundException("User Not Found");
     }
		return new UserPrincipal(user);
	}

}
