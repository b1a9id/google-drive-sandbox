package com.b1a9idps.googledrivesandbox.runner;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.b1a9idps.googledrivesandbox.service.FileService;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

@Component
public class Runner implements CommandLineRunner {
    private static final List<String> SCOPES = List.of(DriveScopes.DRIVE_FILE);
    private static final String CREDENTIALS_FILE_PATH = "/static/credentials.json";

    private final FileService fileService;

    public Runner(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void run(String... args) throws Exception {
        GoogleCredentials credentials;
        try (InputStream inputStream = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream()) {
            credentials = ServiceAccountCredentials.fromStream(inputStream).createScoped(SCOPES);
        }
        credentials.refreshIfExpired();

        fileService.list(credentials);
        fileService.upload(credentials);
        fileService.list(credentials);
    }
}
