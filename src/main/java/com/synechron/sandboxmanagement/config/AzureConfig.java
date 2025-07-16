package com.synechron.sandboxmanagement.config;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {
    
    @Bean
    public DefaultAzureCredential azureCredential() {
        return new DefaultAzureCredentialBuilder()
                .build();
    }
}
