package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.validator.Credential;
import com.gdf.diplomamunka.gaborbeke.nova.validator.UsernameAndEmailValidator;
import com.gdf.diplomamunka.gaborbeke.nova.validator.UsernameAndEmailValidatorChain;
import com.gdf.diplomamunka.gaborbeke.nova.validator.password.Password;
import com.gdf.diplomamunka.gaborbeke.nova.validator.password.PasswordValidatorChain;
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
    private List<UsernameAndEmailValidator> validators;

    private final UsernameAndEmailValidatorChain usernameAndEmailValidatorChain;
    private final PasswordValidatorChain passwordValidatorChain;


    @Autowired
    public RegistrationValidationServiceImpl(UserService userService, UsernameAndEmailValidatorChain usernameAndEmailValidatorChain, PasswordValidatorChain bindedPasswordValidator, PasswordValidatorChain passwordValidatorChain){

        this.userService = userService;
        this.usernameAndEmailValidatorChain = usernameAndEmailValidatorChain;
        this.passwordValidatorChain = passwordValidatorChain;

    }


    @PostConstruct
    public void init(){
        UsernameAndEmailValidator usernameEmailValidator = new UsernameAndEmailValidator("Ez a felhasználónév és email cím már regisztrálva van!", credential -> userService.getUserByUsername(((Credential)credential).getUsername()).isPresent() && userService.getUserByEmail(((Credential)credential).getEmail()).isPresent());
        UsernameAndEmailValidator usernameValidator = new UsernameAndEmailValidator("Ez a felhasználónév már regisztrálva van!", credential -> userService.getUserByUsername(((Credential)credential).getUsername()).isPresent());
        UsernameAndEmailValidator emailValidator = new UsernameAndEmailValidator("Ez az email cím már regisztrálva van!", credential -> userService.getUserByEmail(((Credential)credential).getEmail()).isPresent());
        validators = Arrays.asList(usernameEmailValidator, usernameValidator, emailValidator);
        usernameAndEmailValidatorChain.setValidators(validators);
    }


    @Override
    public Boolean isValidUsernameAndEmail() {
        return usernameAndEmailValidatorChain.isValid(new Credential(user.getUsername(), user.getEmail()));
    }

    @Override
    public Boolean isValidPassword() {
        return passwordValidatorChain.isValid(new Password(user.getPassword()));
    }


    @Override
    public String getErrorMessage() {
        return usernameAndEmailValidatorChain.getMessage();
    }
}


