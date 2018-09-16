package com.gdf.diplomamunka.gaborbeke.nova.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Role role;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "user")
    private List<Ticket> tickets;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isAccountNonLocked = true;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isAccountNonExpired = true;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isCredentialsNonExpired = true;

    @Column(nullable = false)
    private boolean isEnabled = true;


    @Column(nullable = false)
    private LocalDateTime datecreate = LocalDateTime.now();

    @Column(nullable = true)
    private LocalDate dateofbirth;

    public void addTicket(Ticket ticket) {
        if(Objects.nonNull(ticket)) {
            tickets.add(ticket);
            ticket.setUser(this);
        }
    }

    public void removeTicket(Ticket ticket) {
        if(Objects.nonNull(ticket)) {
            ticket.setUser(null);
            tickets.remove(ticket);
        }
    }

    @Override
    public String toString() {
        return lastname+ " "+firstname+" <"+username+">";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}