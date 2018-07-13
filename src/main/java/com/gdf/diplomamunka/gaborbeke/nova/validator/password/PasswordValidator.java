package com.gdf.diplomamunka.gaborbeke.nova.validator.password;

import com.gdf.diplomamunka.gaborbeke.nova.validator.CustomValidator;
import com.gdf.diplomamunka.gaborbeke.nova.validator.CustomValidatorChain;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Arrays;
import java.util.List;

@Data
@ManagedBean
public class PasswordValidator  implements Validator {

    private Integer minimumLength = 8;
    private final Integer maximumLength = 30;
    private List<CustomValidator> passwordValidators;

    @Autowired
    private CustomValidatorChain validatorChain;


    public PasswordValidator(){

    }


    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {

        validatorChain.setValidators(getPasswordValidators());
        if (!validatorChain.isValid(new Password((String) object))){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Érvénytelen regisztráció!", validatorChain.getMessage()));
        }
    }

    private List<CustomValidator> getPasswordValidators(){

        CustomValidator<Password> passwordMinimumLengthValidator = new CustomValidator ("A jelszónak minimum 8 karakterből kell állnia!", credential ->(((Password)credential).getPassword().length()<8));
        CustomValidator<Password> passswordMaximumLengthValidator = new CustomValidator("A jelszó maximum 30 karakterből állhat!", credential -> (((Password)credential).getPassword().length()>30));
        CustomValidator<Password> passwordContainsLowerUpperDigitAndSymbolCharactersValidator = new CustomValidator("A jelszónak tartalmaznia kell kisbetűt, nagybetűt, számot és speciális karaktert!", credential -> !(((Password)credential).getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*(_|[^\\w])).+$")));
        CustomValidator<Password> passwordContainsOnlyWhitespaces = new CustomValidator("A jelszó nem tartalmazhat szóközt!", credential -> (((Password)credential).getPassword().trim().length()==0));
        CustomValidator<Password> passwordContainsWhitespaces = new CustomValidator("A jelszó nem tartalmazhat szóközt!", credential -> (((Password)credential).getPassword().trim().length()!=(((Password)credential).getPassword().length())));
        passwordValidators = Arrays.asList(passwordMinimumLengthValidator, passswordMaximumLengthValidator, passwordContainsLowerUpperDigitAndSymbolCharactersValidator, passwordContainsOnlyWhitespaces, passwordContainsWhitespaces);
        return passwordValidators;
    }
}
