package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void deactivateUser(User user) {

    }

    @Override
    public void activateUser(User user) {

    }

    @Override
    public List<User> getAllUser() {
        return null;
    }

    @Override
    public List<User> getAllActiveUser() {
        return null;
    }

    @Override
    public List<User> getAllDeactivatedUser() {
        return null;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
