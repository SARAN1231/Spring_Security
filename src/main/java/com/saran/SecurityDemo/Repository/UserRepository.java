package com.saran.SecurityDemo.Repository;

import com.saran.SecurityDemo.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users,Integer> {

    Users findByUserName(String username);
}
