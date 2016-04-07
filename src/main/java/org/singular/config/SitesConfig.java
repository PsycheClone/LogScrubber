package org.singular.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "site")
public class SitesConfig {

    private List<String> sites = new ArrayList<String>();

    public List<String> getSites() {
        return sites;
    }
}
