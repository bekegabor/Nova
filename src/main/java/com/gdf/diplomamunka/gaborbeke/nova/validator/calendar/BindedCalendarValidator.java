package com.gdf.diplomamunka.gaborbeke.nova.validator.calendar;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
@ManagedBean
public class BindedCalendarValidator implements Validator {

    private LocalDate minimumDate = LocalDate.now().minusYears(100);
    private LocalDate maximumDate = LocalDate.now().minusYears(18);
    private Integer minimumYearsFromNow;
    private Integer maximumYearsFromNow;
    private final DateOfBirthValidatorChain dateOfBirthValidatorChain;

    @Autowired
    public BindedCalendarValidator(DateOfBirthValidatorChain dateOfBirthValidatorChain) {
        this.dateOfBirthValidatorChain = dateOfBirthValidatorChain;
    }

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
        dateOfBirthValidatorChain.setValidators(getPasswordValidators());

        if (!dateOfBirthValidatorChain.isValid(new DateOfBirth((LocalDate) object))){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Érvénytelen regisztráció!", dateOfBirthValidatorChain.getMessage()));
        }
    }

        private List<DateOfBirthValidator> getPasswordValidators() {
            DateOfBirthValidator dateofBirthValidator = new DateOfBirthValidator("A rendszerbe 18 és 100 év közötti korral lehet regisztrálni!", dateOfBirth -> dateOfBirth.getDateOfBirth().isBefore(minimumDate) || dateOfBirth.getDateOfBirth().isAfter(maximumDate));
            return Arrays.asList(dateofBirthValidator);
        }

    }
