package com.synechron.sandboxmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class SandboxCreateRequest {
    
    @NotBlank(message = "Sandbox name is required")
    private String name;
    
    private String description;
    
    private String acceleratorProgram;
    
    @NotBlank(message = "Environment is required")
    private String environment;
    
    @NotBlank(message = "Namespace is required")
    private String namespace;
    
    private List<String> dnsList;
    
    private String gitRepositoryUrl;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAcceleratorProgram() {
        return acceleratorProgram;
    }
    
    public void setAcceleratorProgram(String acceleratorProgram) {
        this.acceleratorProgram = acceleratorProgram;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    public String getNamespace() {
        return namespace;
    }
    
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public List<String> getDnsList() {
        return dnsList;
    }
    
    public void setDnsList(List<String> dnsList) {
        this.dnsList = dnsList;
    }
    
    public String getGitRepositoryUrl() {
        return gitRepositoryUrl;
    }
    
    public void setGitRepositoryUrl(String gitRepositoryUrl) {
        this.gitRepositoryUrl = gitRepositoryUrl;
    }
}
