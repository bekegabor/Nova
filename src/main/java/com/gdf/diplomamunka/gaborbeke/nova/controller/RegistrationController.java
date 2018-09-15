package com.gdf.diplomamunka.gaborbeke.nova.controller;

import com.gdf.diplomamunka.gaborbeke.nova.enums.Role;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.services.RegistrationValidationService;
import com.gdf.diplomamunka.gaborbeke.nova.services.UserService;
import lombok.Data;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Data
@Scope(value = "request")
@Component(value = "registrationController")
@ELBeanName(value = "registrationController")
@Join(path = "/registration", to = "/registration/registration.xhtml")
public class RegistrationController {

    private User user = new User();
    private List<String> roles;
    private String passwordAgain;
    private final UserService userService;
    private final RegistrationValidationService registrationValidationService;

    @Autowired
    public RegistrationController(RegistrationValidationService registrationValidationService, UserService userService) {
        this.registrationValidationService = registrationValidationService;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        roles = Stream.of(Role.values()).map(Role::name).collect(Collectors.toList());
    }

    public String register(){
        registrationValidationService.setUser(user);
        FacesContext context = FacesContext.getCurrentInstance();
        if (registrationValidationService.isValidUsernameAndEmail() && registrationValidationService.isValidPassword() && registrationValidationService.isValidDateOfBirth()){
            userService.createUser(user);
            user = new User();
            context.addMessage(null, new FacesMessage("", "Sikeres regisztráció! Most már bejelentkezhet a rendszerbe!") );
            context.getExternalContext().getFlash().setKeepMessages(true);
            return "/login?faces-redirect=true";
        }
        if (registrationValidationService.isValidPassword() && registrationValidationService.isValidDateOfBirth()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Érvénytelen regisztráció!", registrationValidationService.getErrorMessage()));
        }

        return "";
    }
}
