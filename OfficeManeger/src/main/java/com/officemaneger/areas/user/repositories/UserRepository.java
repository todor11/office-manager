package com.officemaneger.areas.user.repositories;

import com.officemaneger.areas.role.entities.Role;
import com.officemaneger.areas.user.entities.User;
import com.officemaneger.areas.user.models.interfaces.AccountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository  extends JpaRepository<User,Long> {

    User findOneByUsername(String username);

    @Query("SELECT u.authorities FROM User AS u " +
            "WHERE u.username = :username ")
    List<Role> getUserAuthorities(@Param("username") String username);

    @Query("SELECT u.employee.id FROM User AS u " +
            "WHERE u.username = :username ")
    Long getAccountOwnerIdByUsername(@Param("username") String username);

    @Query("SELECT u.employee.id FROM User AS u " +
            "WHERE u.id = :accountId ")
    Long getAccountOwnerIdByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT u.username FROM User AS u " +
            "WHERE u.id = :accountId ")
    String getUsernameByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT u.id FROM User AS u " +
            "WHERE u.username = :username ")
    Long getAccountIdByUsername(@Param("username") String username);

    @Query("SELECT u.username FROM User AS u " +
            "WHERE u.username = :username")
    String getUsername(@Param("username") String username);

    @Query(value = "SELECT u.id AS id, " +
            "u.username AS username, " +
            "u.isEnabled AS isEnabled " +
            "FROM User AS u " +
            "JOIN u.employee AS e " +
            "WHERE e.id = :employeeId")
    List<AccountView> getAccountsViewByEmployeeId(@Param("employeeId") Long employeeId);

    @Query(value = "SELECT u.username " +
            "FROM User AS u " +
            "JOIN u.employee AS e " +
            "WHERE e.id = :employeeId")
    List<String> getAccountsUsernamesByEmployeeId(@Param("employeeId") Long employeeId);
}
