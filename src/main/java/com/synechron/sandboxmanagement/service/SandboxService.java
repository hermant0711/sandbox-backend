package com.synechron.sandboxmanagement.service;

import com.synechron.sandboxmanagement.dto.SandboxCreateRequest;
import com.synechron.sandboxmanagement.dto.SandboxResponse;
import com.synechron.sandboxmanagement.entity.Sandbox;
import com.synechron.sandboxmanagement.repository.SandboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SandboxService {
    
    @Autowired
    private SandboxRepository sandboxRepository;
    
    public SandboxResponse createSandbox(SandboxCreateRequest request) {
        if (sandboxRepository.existsByName(request.getName())) {
            throw new RuntimeException("Sandbox with name '" + request.getName() + "' already exists");
        }
        
        if (sandboxRepository.existsByNamespace(request.getNamespace())) {
            throw new RuntimeException("Sandbox with namespace '" + request.getNamespace() + "' already exists");
        }
        
        Sandbox sandbox = new Sandbox();
        sandbox.setName(request.getName());
        sandbox.setDescription(request.getDescription());
        sandbox.setAcceleratorProgram(request.getAcceleratorProgram());
        sandbox.setEnvironment(request.getEnvironment());
        sandbox.setNamespace(request.getNamespace());
        sandbox.setDnsList(request.getDnsList());
        sandbox.setGitRepositoryUrl(request.getGitRepositoryUrl());
        
        Sandbox savedSandbox = sandboxRepository.save(sandbox);
        return convertToResponse(savedSandbox);
    }
    
    public List<SandboxResponse> getAllSandboxes() {
        return sandboxRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public Optional<SandboxResponse> getSandboxById(Long id) {
        return sandboxRepository.findById(id)
                .map(this::convertToResponse);
    }
    
    public Optional<SandboxResponse> getSandboxByName(String name) {
        return sandboxRepository.findByName(name)
                .map(this::convertToResponse);
    }
    
    public SandboxResponse updateSandbox(Long id, SandboxCreateRequest request) {
        Sandbox sandbox = sandboxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sandbox not found with id: " + id));
        
        if (!sandbox.getName().equals(request.getName()) && 
            sandboxRepository.existsByName(request.getName())) {
            throw new RuntimeException("Sandbox with name '" + request.getName() + "' already exists");
        }
        
        if (!sandbox.getNamespace().equals(request.getNamespace()) && 
            sandboxRepository.existsByNamespace(request.getNamespace())) {
            throw new RuntimeException("Sandbox with namespace '" + request.getNamespace() + "' already exists");
        }
        
        sandbox.setName(request.getName());
        sandbox.setDescription(request.getDescription());
        sandbox.setAcceleratorProgram(request.getAcceleratorProgram());
        sandbox.setEnvironment(request.getEnvironment());
        sandbox.setNamespace(request.getNamespace());
        sandbox.setDnsList(request.getDnsList());
        sandbox.setGitRepositoryUrl(request.getGitRepositoryUrl());
        
        Sandbox updatedSandbox = sandboxRepository.save(sandbox);
        return convertToResponse(updatedSandbox);
    }
    
    public void deleteSandbox(Long id) {
        if (!sandboxRepository.existsById(id)) {
            throw new RuntimeException("Sandbox not found with id: " + id);
        }
        sandboxRepository.deleteById(id);
    }
    
    private SandboxResponse convertToResponse(Sandbox sandbox) {
        SandboxResponse response = new SandboxResponse();
        response.setId(sandbox.getId());
        response.setName(sandbox.getName());
        response.setDescription(sandbox.getDescription());
        response.setAcceleratorProgram(sandbox.getAcceleratorProgram());
        response.setEnvironment(sandbox.getEnvironment());
        response.setNamespace(sandbox.getNamespace());
        response.setDnsList(sandbox.getDnsList());
        response.setGitRepositoryUrl(sandbox.getGitRepositoryUrl());
        response.setCreatedAt(sandbox.getCreatedAt());
        response.setUpdatedAt(sandbox.getUpdatedAt());
        return response;
    }
}
