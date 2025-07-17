package com.synechron.sandboxmanagement.service;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class KubernetesService {
    
    private static final Logger logger = LoggerFactory.getLogger(KubernetesService.class);
    private final CoreV1Api coreV1Api;
    
    @Value("${kubernetes.config-path:~/.kube/config}")
    private String kubeConfigPath;
    
    public KubernetesService() throws IOException {
        try {
            ApiClient client = createKubeConfigClient();
            Configuration.setDefaultApiClient(client);
            this.coreV1Api = new CoreV1Api();
            logger.info("Kubernetes service initialized with kubeconfig authentication");
        } catch (Exception e) {
            logger.error("Failed to initialize Kubernetes client with kubeconfig: {}", e.getMessage());
            throw new IOException("Failed to authenticate with Kubernetes using kubeconfig", e);
        }
    }
    
    private ApiClient createKubeConfigClient() throws IOException {
        try {
            ApiClient client = Config.defaultClient();
            logger.info("Successfully configured Kubernetes client with kubeconfig from: {}", kubeConfigPath);
            return client;
        } catch (Exception e) {
            logger.error("Failed to load kubeconfig from path: {}", kubeConfigPath);
            throw new IOException("Failed to load kubeconfig", e);
        }
    }
    
    public List<V1Pod> listPodsInNamespace(String namespace) throws ApiException {
        try {
            V1PodList podList = coreV1Api.listNamespacedPod(
                namespace, 
                null, null, null, null, null, null, null, null, null, null
            );
            logger.debug("Successfully listed {} pods in namespace: {}", podList.getItems().size(), namespace);
            return podList.getItems();
        } catch (ApiException e) {
            logger.error("Failed to list pods in namespace '{}': {} - {}", namespace, e.getCode(), e.getResponseBody());
            if (e.getCode() == 403) {
                logger.warn("Access denied to namespace '{}'. Check kubeconfig permissions.", namespace);
            }
            throw e;
        }
    }
    
    public List<V1Service> listServicesInNamespace(String namespace) throws ApiException {
        try {
            V1ServiceList serviceList = coreV1Api.listNamespacedService(
                namespace,
                null, null, null, null, null, null, null, null, null, null
            );
            logger.debug("Successfully listed {} services in namespace: {}", serviceList.getItems().size(), namespace);
            return serviceList.getItems();
        } catch (ApiException e) {
            logger.error("Failed to list services in namespace '{}': {} - {}", namespace, e.getCode(), e.getResponseBody());
            if (e.getCode() == 403) {
                logger.warn("Access denied to namespace '{}'. Check kubeconfig permissions.", namespace);
            }
            throw e;
        }
    }
    
    public String getPodLogs(String podName, String namespace) throws ApiException {
        try {
            String logs = coreV1Api.readNamespacedPodLog(
                podName,
                namespace,
                null, null, null, null, null, null, null, null, null
            );
            logger.debug("Successfully retrieved logs for pod '{}' in namespace: {}", podName, namespace);
            return logs;
        } catch (ApiException e) {
            logger.error("Failed to get logs for pod '{}' in namespace '{}': {} - {}", podName, namespace, e.getCode(), e.getResponseBody());
            if (e.getCode() == 403) {
                logger.warn("Access denied to pod '{}' in namespace '{}'. Check kubeconfig permissions.", podName, namespace);
            }
            throw e;
        }
    }
    
    public V1Pod getPodDescription(String podName, String namespace) throws ApiException {
        try {
            V1Pod pod = coreV1Api.readNamespacedPod(podName, namespace, null);
            logger.debug("Successfully retrieved description for pod '{}' in namespace: {}", podName, namespace);
            return pod;
        } catch (ApiException e) {
            logger.error("Failed to get description for pod '{}' in namespace '{}': {} - {}", podName, namespace, e.getCode(), e.getResponseBody());
            if (e.getCode() == 403) {
                logger.warn("Access denied to pod '{}' in namespace '{}'. Check kubeconfig permissions.", podName, namespace);
            }
            throw e;
        }
    }
    
    public void restartPod(String podName, String namespace) throws ApiException {
        try {
            coreV1Api.deleteNamespacedPod(
                podName,
                namespace,
                null, null, null, null, null, null
            );
            logger.info("Successfully deleted pod '{}' in namespace '{}' for restart", podName, namespace);
        } catch (ApiException e) {
            logger.error("Failed to delete pod '{}' in namespace '{}': {} - {}", podName, namespace, e.getCode(), e.getResponseBody());
            if (e.getCode() == 403) {
                logger.warn("Access denied to delete pod '{}' in namespace '{}'. Check kubeconfig permissions.", podName, namespace);
            }
            throw e;
        }
    }
    
    public V1Service getServiceDescription(String serviceName, String namespace) throws ApiException {
        try {
            V1Service service = coreV1Api.readNamespacedService(serviceName, namespace, null);
            logger.debug("Successfully retrieved description for service '{}' in namespace: {}", serviceName, namespace);
            return service;
        } catch (ApiException e) {
            logger.error("Failed to get description for service '{}' in namespace '{}': {} - {}", serviceName, namespace, e.getCode(), e.getResponseBody());
            if (e.getCode() == 403) {
                logger.warn("Access denied to service '{}' in namespace '{}'. Check kubeconfig permissions.", serviceName, namespace);
            }
            throw e;
        }
    }
    
    public Map<String, Object> getPodInfo(String podName, String namespace) throws ApiException {
        V1Pod pod = getPodDescription(podName, namespace);
        Map<String, Object> podInfo = new HashMap<>();
        
        podInfo.put("name", pod.getMetadata().getName());
        podInfo.put("namespace", pod.getMetadata().getNamespace());
        podInfo.put("status", pod.getStatus().getPhase());
        podInfo.put("creationTimestamp", pod.getMetadata().getCreationTimestamp());
        podInfo.put("labels", pod.getMetadata().getLabels());
        podInfo.put("annotations", pod.getMetadata().getAnnotations());
        
        if (pod.getStatus().getContainerStatuses() != null) {
            podInfo.put("containerStatuses", pod.getStatus().getContainerStatuses());
        }
        
        return podInfo;
    }
    
    public Map<String, Object> getServiceInfo(String serviceName, String namespace) throws ApiException {
        V1Service service = getServiceDescription(serviceName, namespace);
        Map<String, Object> serviceInfo = new HashMap<>();
        
        serviceInfo.put("name", service.getMetadata().getName());
        serviceInfo.put("namespace", service.getMetadata().getNamespace());
        serviceInfo.put("type", service.getSpec().getType());
        serviceInfo.put("clusterIP", service.getSpec().getClusterIP());
        serviceInfo.put("ports", service.getSpec().getPorts());
        serviceInfo.put("selector", service.getSpec().getSelector());
        serviceInfo.put("creationTimestamp", service.getMetadata().getCreationTimestamp());
        serviceInfo.put("labels", service.getMetadata().getLabels());
        
        return serviceInfo;
    }
}
