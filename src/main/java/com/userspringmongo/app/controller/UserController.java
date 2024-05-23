package com.userspringmongo.app.controller;

import com.userspringmongo.app.exception.UserException;
import com.userspringmongo.app.exception.BadRequestException;
import com.userspringmongo.app.model.User;
import com.userspringmongo.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import java.time.Period;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Value("${user.minimum.age}")
    private int minimumAge;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        int age = calculateAge(user.getBirthDate());
        if (age < minimumAge) {
            throw new BadRequestException("User must be at least " + minimumAge + " years old.");
        }
        User createdUser = userRepository.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        if (!userRepository.existsById(id)) {
            throw new UserException("User not found with id " + id);
        }
        updatedUser.setId(id);
        User user = userRepository.save(updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable String id, @RequestBody User partialUpdate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id " + id));
        if (partialUpdate.getEmail() != null) user.setEmail(partialUpdate.getEmail());
        if (partialUpdate.getFirstName() != null) user.setFirstName(partialUpdate.getFirstName());
        if (partialUpdate.getLastName() != null) user.setLastName(partialUpdate.getLastName());
        if (partialUpdate.getBirthDate() != null) user.setBirthDate(partialUpdate.getBirthDate());
        if (partialUpdate.getAddress() != null) user.setAddress(partialUpdate.getAddress());
        if (partialUpdate.getPhoneNumber() != null) user.setPhoneNumber(partialUpdate.getPhoneNumber());
        User updatedUser = userRepository.save(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            throw new UserException("User not found with id " + id);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByBirthDateRange(@RequestParam("from") String fromDate,
                                                                  @RequestParam("to") String toDate) {
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        if (from.isAfter(to)) {
            throw new BadRequestException("\"From\" date must be before \"To\" date");
        }
        List<User> users = userRepository.findByBirthDateBetween(from, to);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
