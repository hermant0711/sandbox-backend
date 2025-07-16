package com.synechron.sandboxmanagement.service;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class KubernetesService {
    
    private final CoreV1Api coreV1Api;
    
    public KubernetesService() throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        this.coreV1Api = new CoreV1Api();
    }
    
    public List<V1Pod> listPodsInNamespace(String namespace) throws ApiException {
        V1PodList podList = coreV1Api.listNamespacedPod(
            namespace, 
            null, null, null, null, null, null, null, null, null, null
        );
        return podList.getItems();
    }
    
    public List<V1Service> listServicesInNamespace(String namespace) throws ApiException {
        V1ServiceList serviceList = coreV1Api.listNamespacedService(
            namespace,
            null, null, null, null, null, null, null, null, null, null
        );
        return serviceList.getItems();
    }
    
    public String getPodLogs(String podName, String namespace) throws ApiException {
        return coreV1Api.readNamespacedPodLog(
            podName,
            namespace,
            null, null, null, null, null, null, null, null, null
        );
    }
    
    public V1Pod getPodDescription(String podName, String namespace) throws ApiException {
        return coreV1Api.readNamespacedPod(podName, namespace, null);
    }
    
    public void restartPod(String podName, String namespace) throws ApiException {
        coreV1Api.deleteNamespacedPod(
            podName,
            namespace,
            null, null, null, null, null, null
        );
    }
    
    public V1Service getServiceDescription(String serviceName, String namespace) throws ApiException {
        return coreV1Api.readNamespacedService(serviceName, namespace, null);
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
