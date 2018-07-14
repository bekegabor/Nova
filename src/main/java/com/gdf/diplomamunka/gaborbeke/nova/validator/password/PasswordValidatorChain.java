package com.gdf.diplomamunka.gaborbeke.nova.validator.password;



import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class PasswordValidatorChain {

    private List<PasswordValidator> validators;
    private String message = "";

    public PasswordValidatorChain(List<PasswordValidator> validators){
        this.validators = validators;
    }

    public PasswordValidatorChain(){

    }

    public Boolean isValid(Password password ){
        for (PasswordValidator testCase : validators){
            if(testCase.isInvalid(password)){
                message = testCase.getMessage();
                return false;
            }
        }
        return true;
    }

}