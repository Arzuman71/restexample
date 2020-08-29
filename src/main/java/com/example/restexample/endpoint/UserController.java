package com.example.restexample.endpoint;

import com.example.restexample.dto.AuthenticationRequest;
import com.example.restexample.dto.AuthenticationResponse;
import com.example.restexample.dto.UserDto;
import com.example.restexample.exception.UserNotFoundException;
import com.example.restexample.model.User;
import com.example.restexample.security.JwtTokenUtil;
import com.example.restexample.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(PasswordEncoder passwordEncoder, UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @PostMapping("/auth")
    public ResponseEntity auth(@RequestBody AuthenticationRequest authenticationRequest) {
        User user = null;
        try {
            user = userService.findByEmail(authenticationRequest.getEmail());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        if (passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            String token = jwtTokenUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .token(token)
                    .userDto(UserDto.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .email(user.getEmail())
                            .userType(user.getUserType())
                            .build())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    @GetMapping
    public List<User> users() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity user(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity saveUser(@RequestBody User user) {
        if (userService.isExists(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok(user.getId());
    }

    @PutMapping("/addImage/{userId}")
    public ResponseEntity addImage(@PathVariable("userId") int userId, @RequestParam(value = "file") MultipartFile file) {
        try {
            User byId = userService.findById(userId);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File uploadFile = new File("D:\\lessons\\JavaGitc2019\\restexample\\image", fileName);
            file.transferTo(uploadFile);
            //user.setImage(filename);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") int id) {
        try {
            userService.findById(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
