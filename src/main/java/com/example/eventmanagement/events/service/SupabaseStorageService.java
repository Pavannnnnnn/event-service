package com.example.eventmanagement.events.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service.key}")
    private String serviceKey;

    private final String bucket = "events-images";

    private final RestTemplate restTemplate;

    public SupabaseStorageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String uploadImage(String fileName, byte[] fileBytes) {

        String uploadUrl = supabaseUrl +
                "/storage/v1/object/" +
                bucket + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceKey);
        headers.set("apikey", serviceKey);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<byte[]> entity =
                new HttpEntity<>(fileBytes, headers);

        restTemplate.exchange(
                uploadUrl,
                HttpMethod.PUT,
                entity,
                String.class
        );

        // return public URL
        return supabaseUrl +
                "/storage/v1/object/public/" +
                bucket + "/" + fileName;
    }
    
    public void deleteImage(String fileUrl) {

        if (fileUrl == null || fileUrl.isEmpty()) return;

        // Extract filename from URL
        String fileName = fileUrl.substring(
                fileUrl.lastIndexOf("/") + 1
        );

        String deleteUrl = supabaseUrl +
                "/storage/v1/object/" +
                bucket + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceKey);
        headers.set("apikey", serviceKey);

        HttpEntity<String> entity =
                new HttpEntity<>(headers);

        restTemplate.exchange(
                deleteUrl,
                HttpMethod.DELETE,
                entity,
                String.class
        );
    }

}
