package com.officemaneger.areas.role.repositories;

import com.officemaneger.areas.role.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findOneByAuthority(String authority);


}
