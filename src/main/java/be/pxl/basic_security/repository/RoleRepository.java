package be.pxl.basic_security.repository;

import be.pxl.basic_security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
}
