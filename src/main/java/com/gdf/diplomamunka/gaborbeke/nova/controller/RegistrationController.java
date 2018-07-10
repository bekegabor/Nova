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
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;
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

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationValidationService registrationValidationService;


    @PostConstruct
    public void init() {
        roles = Stream.of(Role.values()).map(Role::name).collect(Collectors.toList());

    }

    public void register(){
        registrationValidationService.setUser(user);
        FacesContext context = FacesContext.getCurrentInstance();
        if (registrationValidationService.isValidRegistrationCredentials()){
            userService.createUser(user);
            user = new User();
            context.addMessage(null, new FacesMessage("", "Sikeres regisztráció! Most már bejelentkezhet a rendszerbe!") );
            return;
        }

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Érvénytelen regisztráció!", registrationValidationService.getErrorMessage()) );
    }

}
