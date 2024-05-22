package com.userspringmongo.app.repository;

import com.userspringmongo.app.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate);

}
