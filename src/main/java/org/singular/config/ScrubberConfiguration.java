package org.singular.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Configuration
public class ScrubberConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
        placeholderConfigurer.setLocations(new Resource[] {
                new ClassPathResource("application.properties"),
                new FileSystemResource(System.getProperty("user.home") + "/.logscrubber/override.properties")
        });
        placeholderConfigurer.setIgnoreResourceNotFound(true);
        return placeholderConfigurer;
    }
}
