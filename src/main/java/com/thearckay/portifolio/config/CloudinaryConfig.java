package com.thearckay.portifolio.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudnary.secret.name}")
    private String cloudName;
    @Value("${cloudnary.secret.api-key}")
    private String apiKey;
    @Value("${cloudnary.secret.api-secret}")
    private String apiSecret;


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    public String uploadFileAndReturnUrl(MultipartFile file) throws IOException {
        Map uploadResult = this.cloudinary().uploader().upload(file.getBytes(), Collections.emptyMap());
        return uploadResult.get("secure_url").toString();
    }
}
