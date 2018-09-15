package com.gdf.diplomamunka.gaborbeke.nova.validator.fileupload;

import lombok.Data;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Arrays;
import java.util.List;

@Data
@ManagedBean
public class BindedUploadedFileValidator implements Validator {

    private final UploadedFileValidatorChain uploadedFileValidatorChain;

    @Autowired
    public BindedUploadedFileValidator(UploadedFileValidatorChain uploadedFileValidatorChain){
        this.uploadedFileValidatorChain = uploadedFileValidatorChain;
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
        uploadedFileValidatorChain.setValidators(getUploadedFileValidators());
        if (!uploadedFileValidatorChain.isValid((UploadedFile)object)){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "Érvénytelen fájl csatolmány!", uploadedFileValidatorChain.getMessage()));
        }
    }

    private List<UploadedFileValidator> getUploadedFileValidators(){
        List<String> allowedTypes = Arrays.asList("pdf", "jpg", "jpeg");
        UploadedFileValidator fileTypeValidator = new UploadedFileValidator("Érvénytelen fájl csatolmány! A megengedett fájl típusok: pdf, jpg, jpeg", uploadedFile -> !allowedTypes.contains(StringUtils.getFilenameExtension(uploadedFile.getFileName())) && !uploadedFile.getFileName().equals(""));
        UploadedFileValidator fileSizeValidator = new UploadedFileValidator("Érvénytelen fájl csatolmány! A fájl mérete nem lehet nagyobb 5 MB-nál!", uploadedFile -> uploadedFile.getSize()>5000000 );
        return Arrays.asList(fileSizeValidator, fileTypeValidator);
    }
}
