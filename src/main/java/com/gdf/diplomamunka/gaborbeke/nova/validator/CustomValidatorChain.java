package com.gdf.diplomamunka.gaborbeke.nova.validator;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class CustomValidatorChain<T> {

    private List<CustomValidator> validators;
    private String message = "";

    public CustomValidatorChain(List<CustomValidator> validators){
        this.validators = validators;
    }

    public CustomValidatorChain(){

    }

    public Boolean isValid(T t){
        for (CustomValidator testCase : validators){
            if(testCase.isInvalid(t)){
                message = testCase.getMessage();
                return false;
            }
        }
        return true;
    }

    public void addNewValidator(CustomValidator validator){
        validators.add(validator);
    }
}
