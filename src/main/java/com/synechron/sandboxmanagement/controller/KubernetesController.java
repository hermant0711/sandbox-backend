package com.synechron.sandboxmanagement.controller;

import com.synechron.sandboxmanagement.service.KubernetesService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kubernetes")
@CrossOrigin(origins = "http://localhost:3000")
public class KubernetesController {
    
    @Autowired
    private KubernetesService kubernetesService;
    
    @GetMapping("/namespaces/{namespace}/pods")
    public ResponseEntity<List<V1Pod>> listPods(@PathVariable String namespace) {
        try {
            List<V1Pod> pods = kubernetesService.listPodsInNamespace(namespace);
            return new ResponseEntity<>(pods, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/namespaces/{namespace}/services")
    public ResponseEntity<List<V1Service>> listServices(@PathVariable String namespace) {
        try {
            List<V1Service> services = kubernetesService.listServicesInNamespace(namespace);
            return new ResponseEntity<>(services, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/namespaces/{namespace}/pods/{podName}/logs")
    public ResponseEntity<String> getPodLogs(@PathVariable String namespace, 
                                           @PathVariable String podName) {
        try {
            String logs = kubernetesService.getPodLogs(podName, namespace);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error retrieving logs: " + e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/namespaces/{namespace}/pods/{podName}")
    public ResponseEntity<Map<String, Object>> getPodInfo(@PathVariable String namespace, 
                                                         @PathVariable String podName) {
        try {
            Map<String, Object> podInfo = kubernetesService.getPodInfo(podName, namespace);
            return new ResponseEntity<>(podInfo, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/namespaces/{namespace}/pods/{podName}")
    public ResponseEntity<String> restartPod(@PathVariable String namespace, 
                                           @PathVariable String podName) {
        try {
            kubernetesService.restartPod(podName, namespace);
            return new ResponseEntity<>("Pod " + podName + " deleted successfully. " +
                                      "It will be recreated if managed by a deployment.", 
                                      HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>("Error restarting pod: " + e.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/namespaces/{namespace}/services/{serviceName}")
    public ResponseEntity<Map<String, Object>> getServiceInfo(@PathVariable String namespace, 
                                                             @PathVariable String serviceName) {
        try {
            Map<String, Object> serviceInfo = kubernetesService.getServiceInfo(serviceName, namespace);
            return new ResponseEntity<>(serviceInfo, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
