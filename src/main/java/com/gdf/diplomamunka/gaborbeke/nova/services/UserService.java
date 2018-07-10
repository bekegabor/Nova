package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public void createUser(User user);
    public User loadUser(User user);
    public Boolean isUsernameUnavailable(String username);
    public Boolean isEmailUnavailable(String email);
    public void updateUser(User user);
    public void deleteUser(User user);
    public void deactivateUser(User user);
    public void activateUser(User user);
    public List<User> getAllUser();
    public List<User> getAllActiveUser();
    public List<User> getAllDeactivatedUser();
    public Optional<User> getUserByEmail(String email);
    public Optional<User> getUserByUsername(String username);

}
