package com.gdf.diplomamunka.gaborbeke.nova.converter;

import com.gdf.diplomamunka.gaborbeke.nova.model.Role;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("com.gdf.diplomamunka.gaborbeke.nova.converter.RoleConverter")
public class RoleConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String role) {
        return new Role(role);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
        Role role = (Role) object;
        return role.getRole();
    }
}
