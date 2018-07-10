package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.validator.Credential;
import com.gdf.diplomamunka.gaborbeke.nova.validator.Validator;
import com.gdf.diplomamunka.gaborbeke.nova.validator.ValidatorChain;
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

    private final ValidatorChain validatorChain;

    @Autowired
    public RegistrationValidationServiceImpl(UserService userService, ValidatorChain validatorChain){

        this.userService = userService;
        this.validatorChain = validatorChain;
    }


    @PostConstruct
    public void init(){
        Validator usernameEmailValidator = new Validator("Ez a felhasználónév és email cím már regisztrálva van!", credential -> userService.getUserByUsername(credential.getUsername()).isPresent() && userService.getUserByEmail(credential.getEmail()).isPresent());
        Validator usernameValidator = new Validator("Ez a felhasználónév már regisztrálva van!", credential -> userService.getUserByUsername(credential.getUsername()).isPresent());
        Validator emailValidator = new Validator("Ez az email cím már regisztrálva van!", credential -> userService.getUserByEmail(credential.getEmail()).isPresent());
        List<Validator> validators = Arrays.asList(usernameEmailValidator, usernameValidator, emailValidator);
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


