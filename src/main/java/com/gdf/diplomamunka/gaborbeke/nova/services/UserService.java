package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.enums.Status;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void createUser(User user);
    User loadUser(User user);
    Boolean isUsernameUnavailable(String username);
    Boolean isEmailUnavailable(String email);
    void updateUser(User user);
    void updateUserEnabledStatusByUsername(int statusFlag, String username);
    void updateUserRoleByUsername(String role, String username);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);
    List<String> getEmployees();
    List<User> getEmployeesToAssign();
    List<String> getUsers();
    List<String> getBlockedUsers();
    String getUserFriendlyStatus(Status status);

}
