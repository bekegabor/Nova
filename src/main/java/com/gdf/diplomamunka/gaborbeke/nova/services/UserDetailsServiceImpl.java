package com.gdf.diplomamunka.gaborbeke.nova.services;

import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.persistance.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user.isPresent()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(user.get().getRole().getRole()));
            return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
                                                                          user.get().getPassword(),
                                                                          user.get().isEnabled(),
                                                                          user.get().isAccountNonExpired(),
                                                                          user.get().isCredentialsNonExpired(),
                                                                          user.get().isAccountNonLocked(),
                                                                          grantedAuthorities);
        }
        return new org.springframework.security.core.userdetails.User(" ",
                                                                       passwordEncoder.encode(" "),
                                                                       grantedAuthorities);
        }
}

