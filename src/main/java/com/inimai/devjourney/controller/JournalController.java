package com.inimai.devjourney.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.inimai.devjourney.dto.JournalRequest;
import com.inimai.devjourney.dto.JournalResponse;
import com.inimai.devjourney.service.JournalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/journals")
public class JournalController {

    private final JournalService journalService;

    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @PostMapping
    public ResponseEntity<JournalResponse> createJournal(@Valid 
        @RequestBody JournalRequest request) {
            JournalResponse response = journalService.saveJournal(request);
            return ResponseEntity.status(201).body(response);//controller returs a package
    }
    //requestbody does json to dto conversion
    @GetMapping
    public Page<JournalResponse> getAllJournals(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size,
                @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

            Sort sorting = Sort.by(
                    Sort.Direction.fromString(sort[1]),
                    sort[0]
            );
            Pageable pageable = PageRequest.of(page, size, sorting);

            return journalService.getAllJournals(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalResponse> getJournal(@PathVariable Long id) {
        JournalResponse response =journalService.getJournalById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalResponse> updateJournal(
            @PathVariable Long id,
            @Valid @RequestBody JournalRequest request) {

        JournalResponse response = journalService.updateJournal(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJournal(@PathVariable Long id) {
        String response = journalService.deleteJournal(id);
        return ResponseEntity.ok(response);
    }
}