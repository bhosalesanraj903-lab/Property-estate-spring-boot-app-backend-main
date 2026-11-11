package com.properties.real_estate_spring_boot_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dpaa7a0cy");
        config.put("api_key", "597118183129917");
        config.put("api_secret", "6QSbsxFgtkCXLin8Yjp_Bum0v6E");

        return new Cloudinary(config);
    }
}
