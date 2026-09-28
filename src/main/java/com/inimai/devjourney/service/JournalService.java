package com.inimai.devjourney.service;

import java.time.LocalDateTime;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.inimai.devjourney.dto.JournalRequest;
import com.inimai.devjourney.dto.JournalResponse;
import com.inimai.devjourney.entity.Journal;
import com.inimai.devjourney.entity.User;
import com.inimai.devjourney.exception.ResourceNotFoundException;
import com.inimai.devjourney.repository.JournalRepository;

@Service
public class JournalService {

    private final JournalRepository journalRepository;

    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }


    public JournalResponse saveJournal(JournalRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Journal journal = new Journal();

        journal.setTitle(request.getTitle());
        journal.setContent(request.getContent());
        journal.setCreatedAt(LocalDateTime.now());
        journal.setUser(user);

        return mapToResponse(journalRepository.save(journal));
    }


    public JournalResponse getJournalById(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Journal journal = journalRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Journal not found"));

        return mapToResponse(journal);
    }


     public Page<JournalResponse> getAllJournals(Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Page<Journal> journals = journalRepository.findByUser(user, pageable);

        return journals.map(this::mapToResponse);
    }


    public JournalResponse updateJournal(Long id, JournalRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Journal journal = journalRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Journal not found"));

        journal.setTitle(request.getTitle());
        journal.setContent(request.getContent());

        return mapToResponse(journalRepository.save(journal));
    }
    //to understand authentication.whatever()
    //Authentication
     //Principal (Who?)
   //Credentials (Password)
    //Authorities (Roles)
    //Authenticated? (true/false)


    public String deleteJournal(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Journal journal = journalRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Journal not found"));

        journalRepository.delete(journal);

        return "Journal deleted successfully";
    }

    private JournalResponse mapToResponse(Journal journal) {
//maptoresponse takes journal entity and converts it to journalresponse dto outgoing
//spring converts to json and sends to frontend
        JournalResponse response = new JournalResponse();

        response.setId(journal.getId());
        response.setTitle(journal.getTitle());
        response.setContent(journal.getContent());
        response.setCreatedAt(journal.getCreatedAt());

        return response;
    }
}