package com.gdf.diplomamunka.gaborbeke.nova.validator.password;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

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
public class BindedPasswordValidator implements Validator {

    private Integer minimumLength = 8;
    private final Integer maximumLength = 30;
    private List<PasswordValidator> passwordValidators;

    private final PasswordValidatorChain passwordValidatorChain;


    @Autowired
    public BindedPasswordValidator(PasswordValidatorChain passwordValidatorChain){

        this.passwordValidatorChain = passwordValidatorChain;
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
        passwordValidatorChain.setValidators(getPasswordValidators());
        if (!passwordValidatorChain.isValid(new Password((String) object))){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Érvénytelen regisztráció!", passwordValidatorChain.getMessage()));
        }
    }

    private List<PasswordValidator> getPasswordValidators(){

        PasswordValidator passwordMinimumLengthValidator = new PasswordValidator ("A jelszónak minimum 8 karakterből kell állnia!",password -> (((Password)password).getPassword()).length()<8);
        PasswordValidator passswordMaximumLengthValidator = new PasswordValidator("A jelszó maximum 30 karakterből állhat!", credential -> (((Password)credential).getPassword().length()>30));
        PasswordValidator passwordContainsLowerUpperDigitAndSymbolCharactersValidator = new PasswordValidator("A jelszónak tartalmaznia kell legalább kisbetűt, nagybetűt és számot!", credential -> !(((Password)credential).getPassword().matches("^(?=.*[a-z])(?=.*[A-Z]).+$")));
        PasswordValidator passwordContainsOnlyWhitespaces = new PasswordValidator("A jelszó nem tartalmazhat szóközt!", credential -> (((Password)credential).getPassword().trim().length()==0));
        PasswordValidator passwordContainsWhitespaces = new PasswordValidator("A jelszó nem tartalmazhat szóközt!", credential -> (((Password)credential).getPassword().trim().length()!=(((Password)credential).getPassword().length())));
        passwordValidators = Arrays.asList(passwordMinimumLengthValidator, passswordMaximumLengthValidator, passwordContainsLowerUpperDigitAndSymbolCharactersValidator, passwordContainsOnlyWhitespaces, passwordContainsWhitespaces);
        return passwordValidators;
    }
}
