package org.singular.config;

import org.singular.fileManager.FileNameFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScrubberConfiguration {
    @Bean(name = "fileNameFilter")
    public FileNameFilter fileNameFilter() {
        return new FileNameFilter();
    }
}
