package github.bluepsm.joyty.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import github.bluepsm.joyty.models.Role;
import github.bluepsm.joyty.services.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
	@Autowired
	private RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles() {
        Optional<List<Role>> roles = roleService.getAllRoles();
        
        if(roles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(roles.get());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getTagById(@PathVariable Long roleId) {
        Optional<Role> role = roleService.getRoleById(roleId);

        if(role.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(role.get());
    }

    @PostMapping("/create")
    public ResponseEntity<Role> createRole(@RequestBody Role newRole) {
        Optional<Role> createdRoleOpt = roleService.createRole(newRole);
        
        if (createdRoleOpt.isEmpty()) {
        	return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoleOpt.get());
    }

    @PutMapping("/{roleId}/update")
    public ResponseEntity<Role> updateRoleById(@PathVariable Long roleId, @RequestBody Role newRole) {
        Optional<Role> updatedRole = roleService.updateRoleById(roleId, newRole);

        if(updatedRole.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(updatedRole.get());
    }

    @DeleteMapping("/{roleId}/delete")
    public ResponseEntity<?> deleteTagById(@PathVariable Long roleId) {
        if(!roleService.deleteRoleById(roleId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
