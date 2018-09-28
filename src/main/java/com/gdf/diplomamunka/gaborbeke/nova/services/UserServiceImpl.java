package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.enums.Role;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(@Lazy PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User loadUser(User user) {
        return null;
    }

    @Override
    public Boolean isUsernameUnavailable(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public Boolean isEmailUnavailable(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUserEnabledStatusByUsername(int statusFlag, String username) {
        userRepository.updateUserEnabledStatusByUsername(statusFlag, username);
    }

    @Override
    public void updateUserRoleByUsername(String role, String username) {
        userRepository.updateUserRoleByUsername(role, username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<String> getEmployees() {
        return userRepository.findAllByRole(Role.EMPLOYEE.getRoleName());
    }

    @Override
    public List<User> getEmployeesToAssign() {
        return userRepository.findAllEmployee();
    }

    @Override
    public List<String> getUsers() {
        return userRepository.findAllByRole(Role.USER.getRoleName());
    }

    @Override
    public List<String> getBlockedUsers() {
        return userRepository.findAllBlockedUsers();
    }


}
