package com.saran.SecurityDemo.Services;

import com.saran.SecurityDemo.Models.Users;
import com.saran.SecurityDemo.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12); // 12 -> 12 times it is hashed

    public Users RegisterUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
       return userRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
