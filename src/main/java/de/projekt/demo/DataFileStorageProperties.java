package de.projekt.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Set;

@ConfigurationProperties(prefix = "app.image-storage")
@Component
public record DataFileStorageProperties(String basePath,Set<String> allowedMimeTypes) {

    public DataFileStorageProperties(){
        this("./uploads",Set.of("image/pdf"));
    }


}
