package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.model.User;

public interface RegistrationValidationService {
    Boolean isValidUsernameAndEmail();
    Boolean isValidPassword();
    String getErrorMessage();
    void setUser(User user);
}
