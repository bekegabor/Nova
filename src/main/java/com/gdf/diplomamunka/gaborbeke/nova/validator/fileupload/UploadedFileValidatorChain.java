package com.gdf.diplomamunka.gaborbeke.nova.validator.fileupload;

import lombok.Data;
import org.primefaces.model.UploadedFile;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class UploadedFileValidatorChain<T> {

    private List<UploadedFileValidator> validators;
    private String message = "";

    public UploadedFileValidatorChain(List<UploadedFileValidator> validators){
        this.validators = validators;
    }

    public UploadedFileValidatorChain(){}

    public Boolean isValid(UploadedFile uploadedFile){
        for (UploadedFileValidator testCase : validators) {
            if (testCase.isInvalid(uploadedFile)) {
                message = testCase.getMessage();
                return false;
            }
        }
        return true;
    }

    public void addNewValidator(UploadedFileValidator validator){
        validators.add(validator);
    }
}
