package com.saran.SecurityDemo.Controller;

import com.saran.SecurityDemo.Models.Users;
import com.saran.SecurityDemo.Repository.UserRepository;
import com.saran.SecurityDemo.Services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public Users register(@RequestBody Users user) {
      return userService.RegisterUser(user);
    }

    @GetMapping("/user/all-user")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }
}
