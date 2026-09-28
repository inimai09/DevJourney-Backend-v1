package com.inimai.devjourney.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.inimai.devjourney.entity.Journal;
import com.inimai.devjourney.entity.User;

public interface JournalRepository extends JpaRepository<Journal, Long> {
       Page<Journal> findByUser(User user, Pageable pageable);
       //find by user cuz i did many to one mappin
       Optional<Journal> findByIdAndUser(Long id, User user);
       //um to find journal with this ID AND belonging to this user.
}
