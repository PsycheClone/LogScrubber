package org.singular.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(locations = "file:${user.home}/.logscrubber/override.properties", prefix="log")
public class Locations {

    private List<String> locations = new ArrayList<String>();

    public List<String> getLocations() {
        return locations;
    }
}
