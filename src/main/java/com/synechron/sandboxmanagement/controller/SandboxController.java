package com.synechron.sandboxmanagement.controller;

import com.synechron.sandboxmanagement.dto.SandboxCreateRequest;
import com.synechron.sandboxmanagement.dto.SandboxResponse;
import com.synechron.sandboxmanagement.service.SandboxService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sandboxes")
@CrossOrigin(origins = "http://localhost:3000")
public class SandboxController {
    
    @Autowired
    private SandboxService sandboxService;
    
    @PostMapping
    public ResponseEntity<SandboxResponse> createSandbox(@Valid @RequestBody SandboxCreateRequest request) {
        try {
            SandboxResponse response = sandboxService.createSandbox(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<SandboxResponse>> getAllSandboxes() {
        List<SandboxResponse> sandboxes = sandboxService.getAllSandboxes();
        return new ResponseEntity<>(sandboxes, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SandboxResponse> getSandboxById(@PathVariable Long id) {
        Optional<SandboxResponse> sandbox = sandboxService.getSandboxById(id);
        return sandbox.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<SandboxResponse> getSandboxByName(@PathVariable String name) {
        Optional<SandboxResponse> sandbox = sandboxService.getSandboxByName(name);
        return sandbox.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SandboxResponse> updateSandbox(@PathVariable Long id, 
                                                        @Valid @RequestBody SandboxCreateRequest request) {
        try {
            SandboxResponse response = sandboxService.updateSandbox(id, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSandbox(@PathVariable Long id) {
        try {
            sandboxService.deleteSandbox(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
