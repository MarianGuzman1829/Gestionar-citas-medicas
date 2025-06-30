package com.hospital.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class WebConfig {
    
    @Bean
    public HttpMessageConverter<?> jacksonMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }
}
