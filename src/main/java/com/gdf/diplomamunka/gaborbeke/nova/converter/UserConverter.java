package com.gdf.diplomamunka.gaborbeke.nova.converter;

import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.Optional;

@ManagedBean
@RequestScoped
public class UserConverter implements Converter {

    @Autowired
    private UserService userService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String user) {
        return user;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        return object.toString();
    }
}
