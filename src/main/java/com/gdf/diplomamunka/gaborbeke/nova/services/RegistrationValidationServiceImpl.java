package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.validator.Credential;
import com.gdf.diplomamunka.gaborbeke.nova.validator.CustomValidator;
import com.gdf.diplomamunka.gaborbeke.nova.validator.CustomValidatorChain;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
@Data
public class RegistrationValidationServiceImpl implements RegistrationValidationService {

    private final UserService userService;

    private User user;
    private List<CustomValidator> validators;

    private final CustomValidatorChain validatorChain;

    @Autowired
    public RegistrationValidationServiceImpl(UserService userService, CustomValidatorChain validatorChain){

        this.userService = userService;
        this.validatorChain = validatorChain;
    }


    @PostConstruct
    public void init(){
        CustomValidator<Credential> usernameEmailValidator = new CustomValidator("Ez a felhasználónév és email cím már regisztrálva van!", credential -> userService.getUserByUsername(((Credential)credential).getUsername()).isPresent() && userService.getUserByEmail(((Credential)credential).getEmail()).isPresent());
        CustomValidator<Credential> usernameValidator = new CustomValidator("Ez a felhasználónév már regisztrálva van!", credential -> userService.getUserByUsername(((Credential)credential).getUsername()).isPresent());
        CustomValidator<Credential> emailValidator = new CustomValidator("Ez az email cím már regisztrálva van!", credential -> userService.getUserByEmail(((Credential)credential).getEmail()).isPresent());
        validators = Arrays.asList(usernameEmailValidator, usernameValidator, emailValidator);
        validatorChain.setValidators(validators);
    }


    @Override
    public Boolean isValidRegistrationCredentials() {
        return validatorChain.isValid(new Credential(user.getUsername(), user.getEmail()));
    }



    @Override
    public String getErrorMessage() {
        return validatorChain.getMessage();
    }
}


