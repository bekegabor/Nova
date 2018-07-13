package com.gdf.diplomamunka.gaborbeke.nova.validator;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class CustomValidator<T> {

    private String message;
    private Predicate<T>  credentialPredicate;

    public CustomValidator(String message, Predicate<T> credentialPredicate){
        this.message = message;
        this.credentialPredicate = credentialPredicate;
    }

    public Boolean isInvalid(T t){
        return credentialPredicate.test(t);
    }
}
