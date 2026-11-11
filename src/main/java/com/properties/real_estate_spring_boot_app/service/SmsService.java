package com.properties.real_estate_spring_boot_app.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService {

    @Value("${sms.api.key}")
    private String apiKey;

    public void sendOtpSms(String phone, String otp) {

        try {
            String message = "Your OTP for Property Dealer is " + otp;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("authorization", apiKey);

            String body = """
                {
                  "route": "otp",
                  "numbers": "%s",
                  "message": "%s"
                }
                """.formatted(phone, message);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(
                    "https://www.fast2sms.com/dev/bulkV2",
                    entity,
                    String.class
            );

            System.out.println("FAST2SMS RESPONSE: " + response);

        } catch (Exception e) {
            System.err.println("SMS FAILED: " + e.getMessage());
        }
    }
}
