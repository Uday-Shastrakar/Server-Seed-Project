package com.login.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.login.model.Role;



public interface RoleRepository extends JpaRepository<Role, Long> {

	
	
}
