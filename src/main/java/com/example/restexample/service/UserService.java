package com.example.restexample.service;

import com.example.restexample.exception.UserNotFoundException;
import com.example.restexample.model.User;
import com.example.restexample.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isExists(int id) {
        return userRepository.findById(id).isPresent();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteById(int id){
        userRepository.deleteById(id);
    }

    public User findById(int id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }
}
