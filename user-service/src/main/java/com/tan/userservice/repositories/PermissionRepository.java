package com.tan.userservice.repositories;

import com.tan.userservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
	@Query("SELECT p FROM Permission p WHERE lower(p.table_key) = lower(:tableKey)")
	Optional<Permission> findByTableKey(@Param("tableKey") String tableKey);
}
