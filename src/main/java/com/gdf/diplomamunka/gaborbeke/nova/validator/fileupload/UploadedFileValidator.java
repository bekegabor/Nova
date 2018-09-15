package com.gdf.diplomamunka.gaborbeke.nova.validator.fileupload;

import lombok.Data;
import org.primefaces.model.UploadedFile;

import javax.annotation.ManagedBean;
import java.util.function.Predicate;

@Data
public class UploadedFileValidator {

    private String message;
    private Predicate<UploadedFile> uploadedFilePredicate;

    public UploadedFileValidator(String message, Predicate<UploadedFile> uploadedFilePredicate){
        this.message = message;
        this.uploadedFilePredicate = uploadedFilePredicate;
    }

    public Boolean isInvalid(UploadedFile uploadedFile){
        return uploadedFilePredicate.test(uploadedFile);
    }
}
