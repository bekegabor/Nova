package com.gdf.diplomamunka.gaborbeke.nova.validator.fileupload;

import lombok.Data;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

@Data
@ManagedBean
public class BindedUploadedFileValidator implements Validator {

    private static final int PDF_MIME_TYPE = 0x25504446;
    private static final int JPEG_MIME_TYPE = 0xffd8ffe0;

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
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }

    private List<UploadedFileValidator> getUploadedFileValidators(){
        List<String> allowedTypes = Arrays.asList("pdf", "jpg", "jpeg");
        UploadedFileValidator fileTypeValidator = new UploadedFileValidator("Érvénytelen fájl csatolmány! A megengedett fájl típusok: pdf, jpg, jpeg", uploadedFile -> !isInvalidFileType(uploadedFile));
        UploadedFileValidator fileSizeValidator = new UploadedFileValidator("Érvénytelen fájl csatolmány! A fájl mérete nem lehet nagyobb 5 MB-nál!", uploadedFile -> Objects.isNull(uploadedFile) );
        return Arrays.asList(fileSizeValidator, fileTypeValidator);
    }

    private Boolean isInvalidFileType(UploadedFile uploadedFile){
        int test = 0;
        List<String> allowedTypes = Arrays.asList("pdf", "jpg");
        Map<Integer, String> mimeTypeNames = new HashMap<>();
        mimeTypeNames.put(PDF_MIME_TYPE, "pdf");
        mimeTypeNames.put(JPEG_MIME_TYPE, "jpg");
        String fileExtension = StringUtils.getFilenameExtension(uploadedFile.getFileName());
        if (!uploadedFile.getFileName().equals("")) {
            try {
                DataInputStream dataInputStream = new DataInputStream(uploadedFile.getInputstream());
                test = dataInputStream.readInt();
            } catch (IOException e) {
                throw new RuntimeException("Error occured during reading bytes... ", e);
            }
            Boolean isAllowedFileExtension = allowedTypes.contains(fileExtension);
            Boolean isAllowedMimeType = (test == PDF_MIME_TYPE) || (test == JPEG_MIME_TYPE);

            return isAllowedFileExtension && isAllowedMimeType && mimeTypeNames.get(test).equals(fileExtension);
        }
        return true;
    }
}
