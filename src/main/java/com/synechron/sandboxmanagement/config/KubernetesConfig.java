package com.synechron.sandboxmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class KubernetesConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(KubernetesConfig.class);
    
    public KubernetesConfig() {
        logger.info("Kubernetes configuration initialized - using kubeconfig authentication");
    }
}
