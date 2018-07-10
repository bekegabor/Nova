package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.model.User;

public interface RegistrationValidationService {
    public Boolean isValidRegistrationCredentials();
    public String getErrorMessage();
    public void setUser(User user);
}
