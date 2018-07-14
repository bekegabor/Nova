package com.gdf.diplomamunka.gaborbeke.nova.validator.calendar;

import com.gdf.diplomamunka.gaborbeke.nova.validator.password.Password;
import lombok.Data;

import java.util.function.Predicate;

@Data
public class DateOfBirthValidator {

    private String message;
    private Predicate<DateOfBirth> credentialPredicate;

    public DateOfBirthValidator(String message, Predicate<DateOfBirth> credentialPredicate){
        this.message = message;
        this.credentialPredicate = credentialPredicate;
    }

    public Boolean isInvalid(DateOfBirth dateOfBirth){
        return credentialPredicate.test(dateOfBirth);
    }
}
