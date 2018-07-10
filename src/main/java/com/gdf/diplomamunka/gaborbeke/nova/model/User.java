package com.gdf.diplomamunka.gaborbeke.nova.model;

import com.gdf.diplomamunka.gaborbeke.nova.enums.Role;
import lombok.Data;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy=AUTO)
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

    @Column(nullable = false)
    @Enumerated(STRING)
    private Role role;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false)
    private LocalDateTime datecreate = LocalDateTime.now();

    @Column(nullable = true)
    private Date dateofbirth;

}