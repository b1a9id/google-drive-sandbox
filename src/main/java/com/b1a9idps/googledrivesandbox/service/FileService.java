package com.b1a9idps.googledrivesandbox.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.auth.oauth2.GoogleCredentials;

public interface FileService {
    void list(GoogleCredentials credentials) throws IOException, GeneralSecurityException;

    void upload(GoogleCredentials credentials) throws IOException, GeneralSecurityException;
}
