package millet.demo.service;

import millet.demo.models.Student;
import millet.demo.repo.StudentRrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentRrepository studentRrepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = studentRrepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(student.getEmail(), student.getPassword(), Collections.emptyList());
    }
}
