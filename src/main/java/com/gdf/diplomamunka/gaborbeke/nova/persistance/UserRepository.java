package com.gdf.diplomamunka.gaborbeke.nova.persistance;
import com.gdf.diplomamunka.gaborbeke.nova.model.Role;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT username FROM user INNER JOIN role ON user.id = role.id WHERE role = ?1", nativeQuery = true)
    List<String> findAllByRole(String role);

    @Query(value = "SELECT * FROM user INNER JOIN role ON user.id = role.id WHERE role = 'EMPLOYEE'", nativeQuery = true)
    List<User> findAllEmployee();

    @Query(value = "SELECT username FROM user WHERE user.is_enabled = 0", nativeQuery = true)
    List<String> findAllBlockedUsers();

    Optional<User> findByEmail(String email);

    User save(User user);

    @Transactional
    @Modifying
    @Query(value = "UPDATE user SET user.is_enabled = ?1 WHERE user.username = ?2", nativeQuery = true)
    void updateUserEnabledStatusByUsername(int statusFlag, String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE role inner join user on role.id = user.id set role.role = ?1 where user.username = ?2 ", nativeQuery = true)
    void updateUserRoleByUsername(String role, String username);

}