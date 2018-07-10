package com.gdf.diplomamunka.gaborbeke.nova.validator;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class Validator {

    private String message;
    private Predicate<Credential>  credentialPredicate;

    public Validator(String message, Predicate<Credential> credentialPredicate){
        this.message = message;
        this.credentialPredicate = credentialPredicate;
    }

    public Boolean isInvalid(Credential credential){
        return credentialPredicate.test(credential);
    }
}
