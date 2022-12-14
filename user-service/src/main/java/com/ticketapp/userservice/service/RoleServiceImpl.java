package com.ticketapp.userservice.service;

import com.ticketapp.userservice.dao.entity.Permission;
import com.ticketapp.userservice.dao.entity.Role;
import com.ticketapp.userservice.dao.repo.PermissionRepository;
import com.ticketapp.userservice.dao.repo.RoleRepository;
import com.ticketapp.userservice.dto.PermissionDTO;
import com.ticketapp.userservice.dto.RoleDTO;
import com.ticketapp.userservice.exception.PermissionNotFoundException;
import com.ticketapp.userservice.exception.RoleNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl extends AbstractService implements RoleService {

    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(ModelMapper modelMapper, RoleRepository repository, PermissionRepository permissionRepository) {
        super(modelMapper);
        this.repository = repository;
        this.permissionRepository = permissionRepository;
    }


    public RoleDTO save(RoleDTO roleDTO){
        Role roleEntity = modelMapper.map(roleDTO, Role.class);
        roleEntity.setRole(roleDTO.getRole().toUpperCase());

        Role savedRole = repository.save(roleEntity);
        return modelMapper.map(savedRole, RoleDTO.class);
    }

    public List<RoleDTO> findAll(){
        List<Role> roles = repository.findAll();
        return roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .toList();
    }

    public RoleDTO findById(Long id){
        return modelMapper.map(getById(id), RoleDTO.class);
    }

    public List<PermissionDTO> findPermissionsById(Long id){
        Role role = getById(id);

        return role.getPermissions()
                .stream()
                .map(permission -> modelMapper.map(permission, PermissionDTO.class))
                .toList();
    }

    public void addPermissionToRole(Long id, Long permissionId){
        Permission permission = getPermissionById(permissionId);
        Role role = getById(id);
        role.getPermissions().add(permission);
        repository.save(role);
    }

    public void deletePermissionToRole(Long id, Long permissionId){
        Permission permission = getPermissionById(permissionId);
        Role role = getById(id);
        role.getPermissions().remove(permission);
        repository.save(role);
    }



    public RoleDTO update(Long id, RoleDTO roleRequestDTO){
        Role role = getById(id);
        role.setRole(roleRequestDTO.getRole().toUpperCase());
        role.setDescription(roleRequestDTO.getDescription());

        repository.save(role);
        return modelMapper.map(role, RoleDTO.class);
    }

    public void delete(Long id){
        repository.delete(getById(id));
    }

    private Role getById(Long id){
        Optional<Role> optionalRole = repository.findById(id);
        if(optionalRole.isEmpty()) throw new RoleNotFoundException(id);
        return optionalRole.get();
    }

    private Permission getPermissionById(Long permissionId){
        Optional<Permission> optionalPermission =  permissionRepository.findById(permissionId);
        if (optionalPermission.isEmpty()) throw new PermissionNotFoundException(permissionId);
        return optionalPermission.get();
    }
}
