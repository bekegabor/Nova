package com.gdf.diplomamunka.gaborbeke.nova.services;

import org.primefaces.model.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

public interface FileService {

    byte[] getAttachmentAsBytes(UploadedFile uploadedFile);
    Blob getAttachmentAsBlob(UploadedFile uploadedFile) throws SQLException;
    InputStream getAttachmentAsInputStream(UploadedFile uploadedFile) throws IOException;
    String getAttachmentName(UploadedFile uploadedFile);
    Long getAttachmentSize(UploadedFile uploadedFile);
    Boolean isValidFileType();
    Boolean isValidFileSize();
    UploadedFile getUploadedFile();
    void setUploadedFile(UploadedFile uploadedFile);
    String getErrorMessage();
    List<String> readTextFileFromClasspath() throws IOException, URISyntaxException;

}
