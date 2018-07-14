package com.gdf.diplomamunka.gaborbeke.nova.validator;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class UsernameAndEmailValidatorChain {

    private List<UsernameAndEmailValidator> validators;
    private String message = "";

    public UsernameAndEmailValidatorChain(List<UsernameAndEmailValidator> validators){
        this.validators = validators;
    }

    public UsernameAndEmailValidatorChain(){

    }

    public Boolean isValid(Credential credential ){
        for (UsernameAndEmailValidator testCase : validators){
            if(testCase.isInvalid(credential)){
                message = testCase.getMessage();
                return false;
            }
        }
        return true;
    }

    public void addNewValidator(UsernameAndEmailValidator validator){
        validators.add(validator);
    }
}
