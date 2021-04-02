package com.b1a9idps.googledrivesandbox.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("gdrive")
@ConstructorBinding
public final class GDriveProperties {
    private final String parentDirId;
    private final String downloadFileId;

    public GDriveProperties(String parentDirId, String downloadFileId) {
        this.parentDirId = parentDirId;
        this.downloadFileId = downloadFileId;
    }

    public String getParentDirId() {
        return parentDirId;
    }

    public String getDownloadFileId() {
        return downloadFileId;
    }
}
