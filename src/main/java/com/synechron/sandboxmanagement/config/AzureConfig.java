package com.synechron.sandboxmanagement.config;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class AzureConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(AzureConfig.class);
    
    @Value("${azure.tenant-id:}")
    private String tenantId;
    
    @Value("${azure.client-id:}")
    private String clientId;
    
    @Bean
    public DefaultAzureCredential azureCredential() {
        DefaultAzureCredentialBuilder builder = new DefaultAzureCredentialBuilder();
        
        if (!tenantId.isEmpty()) {
            builder.tenantId(tenantId);
            logger.info("Azure tenant ID configured: {}", tenantId);
        }
        
        if (!clientId.isEmpty()) {
            builder.managedIdentityClientId(clientId);
            logger.info("Azure client ID configured: {}", clientId);
        }
        
        DefaultAzureCredential credential = builder.build();
        logger.info("Azure default credential initialized successfully");
        
        return credential;
    }
}
