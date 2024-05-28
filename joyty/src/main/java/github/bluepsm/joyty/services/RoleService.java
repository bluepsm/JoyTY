package github.bluepsm.joyty.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import github.bluepsm.joyty.models.Role;
import github.bluepsm.joyty.repositories.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;

    public Optional<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return Optional.of(roles);
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> createRole(Role role) {
        return Optional.of(roleRepository.save(role));
    }

    public Optional<Role> updateRoleById(Long id, Role role) {
        Optional<Role> roleOpt = roleRepository.findById(id);

        if (roleOpt.isEmpty()) {
            return Optional.empty();
        }

        role.setId(id);

        return Optional.of(roleRepository.save(role));
    }

    public boolean deleteRoleById(Long id) {
        try {
            roleRepository.deleteById(id);
            return true;
        } catch(EmptyResultDataAccessException err) {
            return false;
        }
    }
}
