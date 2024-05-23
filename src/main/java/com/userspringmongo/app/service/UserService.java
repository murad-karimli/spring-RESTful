package com.userspringmongo.app.service;

import com.userspringmongo.app.model.User;
import com.userspringmongo.app.exception.UserException;
import com.userspringmongo.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String id, User updatedUser) {
        updatedUser.setId(id);
        return userRepository.save(updatedUser);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public List<User> searchUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        return userRepository.findByBirthDateBetween(fromDate, toDate);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException("User not found"));
    }
}
