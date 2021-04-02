package com.b1a9idps.googledrivesandbox.runner;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class Runner implements CommandLineRunner {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_METADATA_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/static/credentials.json";

    @Override
    public void run(String... args) throws Exception {
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(getCredentials());
        Drive service = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, requestInitializer)
                .setApplicationName("Google Drive Sandbox")
                .build();

        FileList result = service.files().list().setPageSize(10).execute();
        List<File> files = result.getFiles();
        if (CollectionUtils.isEmpty(files)) {
            log.info("No files found.");
            return;
        }
        log.info("Files:");
        files.forEach(file -> log.info("file name: {}, id: {}\n", file.getName(), file.getId()));
    }

    private static GoogleCredentials getCredentials() throws IOException {
        try (InputStream inputStream = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream()) {
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(inputStream).createScoped(SCOPES);
            credentials.refreshIfExpired();
            return credentials;
        }
    }
}
