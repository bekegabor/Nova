package com.gdf.diplomamunka.gaborbeke.nova.validator.password;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class PasswordValidator {


    private String message;
    private Predicate<Password> credentialPredicate;

    public PasswordValidator(String message, Predicate<Password> credentialPredicate){
        this.message = message;
        this.credentialPredicate = credentialPredicate;
    }

    public Boolean isInvalid(Password password){
        return credentialPredicate.test(password);
    }


}
