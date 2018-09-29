package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.validator.fileupload.UploadedFileValidator;
import com.gdf.diplomamunka.gaborbeke.nova.validator.fileupload.UploadedFileValidatorChain;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServiceImpl implements FileService {

    @Getter
    @Setter
    private final UploadedFileValidatorChain uploadedFileValidatorChain;

    private UploadedFile uploadedFile;

    @Autowired
    public FileServiceImpl(UploadedFileValidatorChain uploadedFileValidatorChain){
        this.uploadedFileValidatorChain = uploadedFileValidatorChain;
    }

    @PostConstruct
    public void init(){
        List<String> allowedTypes = Arrays.asList("pdf", "jpg", "jpeg");
        UploadedFileValidator fileTypeValidator = new UploadedFileValidator("A megengedett fájl típusok: pdf, jpg, jpeg", uploadedFile -> !allowedTypes.contains(StringUtils.getFilenameExtension(uploadedFile.getFileName())) && !uploadedFile.getFileName().equals(""));
        UploadedFileValidator fileSizeValidator = new UploadedFileValidator("A fájl mérete nem lehet nagyobb 5 MB-nál!", uploadedFile -> uploadedFile.getSize()>5000000);
    }

    @Override
    public byte[] getAttachmentAsBytes(UploadedFile uploadedFile) {
        return new byte[0];
    }

    @Override
    public Blob getAttachmentAsBlob(UploadedFile uploadedFile) throws SQLException {
        return new javax.sql.rowset.serial.SerialBlob(uploadedFile.getContents());
    }

    @Override
    public InputStream getAttachmentAsInputStream(UploadedFile uploadedFile) throws IOException {
        return null;
    }

    @Override
    public String getAttachmentName(UploadedFile uploadedFile) {
        return null;
    }

    @Override
    public Long getAttachmentSize(UploadedFile uploadedFile) {
        return null;
    }

    @Override
    public Boolean isValidFileType() {
        return uploadedFileValidatorChain.isValid(uploadedFile);
    }

    @Override
    public Boolean isValidFileSize() {
        return uploadedFileValidatorChain.isValid(uploadedFile);
    }

    @Override
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    @Override
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    @Override
    public String getErrorMessage() {
        return uploadedFileValidatorChain.getMessage();
    }

    @Override
    public List<String> readTextFileFromClasspath() throws IOException, URISyntaxException {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("ConsoleCommands.txt");
        List<String> helpContent = readFromInputStream(inputStream);
        return helpContent;
    }
    private List<String> readFromInputStream(InputStream inputStream)throws IOException {
        List<String> helpContent = new ArrayList<>();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                helpContent.add(line);
            }
        }
        return helpContent;
    }
}
