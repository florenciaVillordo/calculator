package com.tenpo.calculator.security.repository;

import com.tenpo.calculator.security.model.Role;
import com.tenpo.calculator.security.model.RoleType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByType(RoleType type);
}
