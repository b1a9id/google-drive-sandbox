package com.b1a9idps.googledrivesandbox.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("gdrive")
@ConstructorBinding
public final class GDriveProperties {
    private final String parentDirId;

    public GDriveProperties(String parentDirId) {
        this.parentDirId = parentDirId;
    }

    public String getParentDirId() {
        return parentDirId;
    }
}
