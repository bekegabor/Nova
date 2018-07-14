package com.gdf.diplomamunka.gaborbeke.nova.validator.calendar;

import com.gdf.diplomamunka.gaborbeke.nova.validator.password.Password;
import com.gdf.diplomamunka.gaborbeke.nova.validator.password.PasswordValidator;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class DateOfBirthValidatorChain {
    private List<DateOfBirthValidator> validators;
    private String message = "";

    public DateOfBirthValidatorChain(List<DateOfBirthValidator> validators){
        this.validators = validators;
    }

    public DateOfBirthValidatorChain(){

    }

    public Boolean isValid(DateOfBirth dateOfBirth ){
        for (DateOfBirthValidator testCase : validators){
            if(testCase.isInvalid(dateOfBirth)){
                message = testCase.getMessage();
                return false;
            }
        }
        return true;
    }
}
