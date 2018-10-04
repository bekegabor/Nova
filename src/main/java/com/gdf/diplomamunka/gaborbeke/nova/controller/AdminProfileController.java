package com.gdf.diplomamunka.gaborbeke.nova.controller;


import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.services.RegistrationValidationService;
import com.gdf.diplomamunka.gaborbeke.nova.services.UserService;
import lombok.Data;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

@Data
@Scope("session")
@Component(value = "adminProfileController")
@ELBeanName(value = "adminProfileController")
@Join(path = "/admin/profile", to = "/adminprofile/adminprofile.xhtml")
public class AdminProfileController {

    private User user = new User();
    private final UserService userService;
    private final RegistrationValidationService registrationValidationService;
    private String password;

    @Autowired
    public AdminProfileController(UserService userService, RegistrationValidationService registrationValidationService){
        this.userService = userService;
        this.registrationValidationService = registrationValidationService;
    }

    public void save() throws IOException {
        String currentUserName = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        FacesContext context = FacesContext.getCurrentInstance();
        user = userService.getUserByUsername(currentUserName).get();
        user.setPassword(password);
        registrationValidationService.setUser(user);

        if (registrationValidationService.isValidPassword()){
            userService.updateUser(user);
            context.addMessage(null, new FacesMessage("", "A jelszó sikeresen meg lett változtatva!") );
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()+"/admin/home.xhtml");
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "A jelszó változtatás sikertelen volt! "+registrationValidationService.getErrorMessage()));
    }
}
