package com.b1a9idps.googledrivesandbox.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.b1a9idps.googledrivesandbox.properties.GDriveProperties;
import com.b1a9idps.googledrivesandbox.service.FileService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final GDriveProperties gDriveProperties;

    public FileServiceImpl(GDriveProperties gDriveProperties) {
        this.gDriveProperties = gDriveProperties;
    }

    @Override
    public void list(GoogleCredentials credentials) throws IOException, GeneralSecurityException {
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        Drive service = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, requestInitializer)
                .setApplicationName("Google Drive Sandbox")
                .build();

        FileList result = service.files().list().setPageSize(10).execute();
        List<File> files = result.getFiles();
        if (CollectionUtils.isEmpty(files)) {
            LOG.info("No files found.");
            return;
        }
        LOG.info("Files:");
        files.forEach(file -> LOG.info("file name: {}, id: {}\n", file.getName(), file.getId()));
    }

    @Override
    public void upload(GoogleCredentials credentials) throws IOException, GeneralSecurityException {
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        Drive service = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, requestInitializer)
                .setApplicationName("Google Drive Sandbox")
                .build();

        File fileMetadata = new File();
        fileMetadata.setName("create.txt");
        fileMetadata.setParents(Collections.singletonList(gDriveProperties.getParentDirId()));
        FileContent mediaContent = new FileContent("text/plain", new ClassPathResource("/static/create.txt").getFile());
        File file = service.files().create(fileMetadata, mediaContent)
                .setFields("id, parents")
                .execute();

        LOG.info("Uploaded: file id: {}\n", file.getId());
    }

    @Override
    public void download(GoogleCredentials credentials) throws IOException, GeneralSecurityException {
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        Drive service = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, requestInitializer)
                .setApplicationName("Google Drive Sandbox")
                .build();

        File file = service.files().get(gDriveProperties.getDownloadFileId()).execute();
        LOG.info("Downloaded: file id: {}, file name: {}", file.getId(), file.getName());
    }
}
