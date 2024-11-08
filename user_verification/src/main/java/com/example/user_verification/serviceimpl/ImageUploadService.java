package com.example.user_verification.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService {

    private static final String IMGBB_API_URL = "https://api.imgbb.com/1/upload";
    private static final String API_KEY = "088ea0435607af67b5f2a81c95a491d5";

    public String uploadImage(MultipartFile file) {

        try {

            String base64Image = java.util.Base64.getEncoder().encodeToString(file.getBytes());

            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("key", API_KEY);
            params.add("image", base64Image);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(IMGBB_API_URL, HttpMethod.POST, requestEntity, String.class);

            String imageUrl = parseImageUrlFromResponse(response.getBody());
            return imageUrl;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseImageUrlFromResponse(String response) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response);
            String imageUrl = jsonResponse.get("data").get("url").asText();
            return imageUrl;
        } catch (JsonProcessingException e) {

            e.printStackTrace();
            return null;
        }
    }

//class ends here
}
