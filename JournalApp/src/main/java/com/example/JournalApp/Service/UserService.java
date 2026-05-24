package com.example.JournalApp.Service;

import com.example.JournalApp.Entry.User;
import com.example.JournalApp.dto.PasswordUpdateRequest;
import com.example.JournalApp.dto.UsernameUpdateRequest;
import com.example.JournalApp.repository.UserRepository;
import com.example.JournalApp.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User saveNewUser(User user){

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );

        user.setRoles(Arrays.asList("USER"));

        return userRepository.save(user);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User findByusername(String username){
        return userRepository.findByusername(username);
    }

    public void deleteByUsername(String username){
        userRepository.deleteByusername(username);
    }

    // UPDATE USERNAME
    public void updateUsername(
            UsernameUpdateRequest request,
            HttpServletResponse response){

        String currentUsername =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        User userInDb =
                findByusername(currentUsername);

        if(userInDb == null){
            throw new RuntimeException("User not found");
        }

        if(request.getUsername() == null ||
                request.getUsername().trim().isEmpty()){

            throw new RuntimeException("Username required");
        }

        userInDb.setUsername(request.getUsername());

        saveUser(userInDb);

        // Generate NEW JWT
        String token =
                jwtUtil.generateToken(
                        userInDb.getUsername()
                );

        addJwtCookie(token, response);
    }

    // UPDATE PASSWORD
    public void updatePassword(
            PasswordUpdateRequest request,
            HttpServletResponse response){

        String currentUsername =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        User userInDb =
                findByusername(currentUsername);

        if(userInDb == null){
            throw new RuntimeException("User not found");
        }

        if(request.getPassword() == null ||
                request.getPassword().trim().isEmpty()){

            throw new RuntimeException("Password required");
        }

        userInDb.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        saveUser(userInDb);

        // Generate NEW JWT
        String token =
                jwtUtil.generateToken(
                        userInDb.getUsername()
                );

        addJwtCookie(token, response);
    }

    // COMMON COOKIE METHOD
    private void addJwtCookie(
            String token,
            HttpServletResponse response){

        Cookie cookie =
                new Cookie("jwt", token);

        cookie.setHttpOnly(true);

        cookie.setSecure(false);

        cookie.setPath("/");

        // 2 minutes
        cookie.setMaxAge(2 * 60);

        response.addCookie(cookie);
    }
}