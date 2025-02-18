package com.saran.SecurityDemo.Services;

import com.saran.SecurityDemo.Models.UserPrincipal;
import com.saran.SecurityDemo.Models.Users;
import com.saran.SecurityDemo.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// responsible for own user detail verification
@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUserName(username);

        if(user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException(username);
        }
        // we need to return userdetails obj since it is interface we can't create a obj so we create a userprincipal class which implements Userdetails
        return new UserPrincipal(user);
    }
}
