package com.userspringmongo.app.controller;

import com.userspringmongo.app.exception.UserException;
import com.userspringmongo.app.exception.BadRequestException;
import com.userspringmongo.app.model.User;
import com.userspringmongo.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        if (!userRepository.existsById(id)) {
            throw new UserException("User not found with id " + id);
        }
        updatedUser.setId(id);
        return userRepository.save(updatedUser);
    }

    @PatchMapping("/{id}")
    public User patchUser(@PathVariable String id, @RequestBody User partialUpdate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with id " + id));
        if (partialUpdate.getEmail() != null) user.setEmail(partialUpdate.getEmail());
        if (partialUpdate.getFirstName() != null) user.setFirstName(partialUpdate.getFirstName());
        if (partialUpdate.getLastName() != null) user.setLastName(partialUpdate.getLastName());
        if (partialUpdate.getBirthDate() != null) user.setBirthDate(partialUpdate.getBirthDate());
        if (partialUpdate.getAddress() != null) user.setAddress(partialUpdate.getAddress());
        if (partialUpdate.getPhoneNumber() != null) user.setPhoneNumber(partialUpdate.getPhoneNumber());
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            throw new UserException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    @GetMapping("/search")
    public List<User> searchUsersByBirthDateRange(@RequestParam("from") String fromDate,
                                                  @RequestParam("to") String toDate) {
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        if (from.isAfter(to)) {
            throw new BadRequestException("\"From\" date must be before \"To\" date");
        }

        return userRepository.findByBirthDateBetween(from, to);
    }
}
