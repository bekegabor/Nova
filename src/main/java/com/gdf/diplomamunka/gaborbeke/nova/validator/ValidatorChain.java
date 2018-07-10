package com.gdf.diplomamunka.gaborbeke.nova.validator;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Data
@Component
public class ValidatorChain {

    private List<Validator> validators;
    private String message = "";

    public ValidatorChain(List<Validator> validators){
        this.validators = validators;
    }

    public ValidatorChain(){

    }

    public Boolean isValid(Credential credential){
        for (Validator testCase : validators){
            if(testCase.isInvalid(credential)){
                message = testCase.getMessage();
                return false;
            }
        }
        return true;
    }
}
