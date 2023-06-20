package my.project.userservicemain.service;

import lombok.RequiredArgsConstructor;
import my.project.userservicemain.entities.Role;
import my.project.userservicemain.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(Role role) {
        Role roleEntity = getById(role.getId());
        roleEntity.setName(role.getName());
        return roleEntity;
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    public Role getById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role findByName(String name) {
        return roleRepository.findOneByName(name);
    }
}