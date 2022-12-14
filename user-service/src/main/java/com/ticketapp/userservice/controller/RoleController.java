package com.ticketapp.userservice.controller;

import com.ticketapp.userservice.dto.PermissionDTO;
import com.ticketapp.userservice.dto.RoleDTO;
import com.ticketapp.userservice.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class RoleController extends AbstractController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @Tag(name = "Roles")
    @Operation(
            method = "POST",
            summary = "Create a new role",
            description = "Create a new role"
    )
    @ApiResponse(responseCode = "201",
            description = "successfully created a new resource role",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RoleDTO.class)
            )
    )
    @PostMapping(path = "/roles")
    public ResponseEntity<RoleDTO> addRole(@Valid @RequestBody RoleDTO role){
        log.info("Adding a role with name {} description {}", role.getRole(), role.getDescription());
        return new ResponseEntity<>(service.save(role), HttpStatus.CREATED);
    }

    @Tag(name = "Roles")
    @Operation(
            method = "GET",
            summary = "Get role by id",
            description = "Get role by id"
    )
    @GetMapping(path = "/roles/{id}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable Long id) {
        log.info("Request for get role by id {}", id);
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @Tag(name = "Roles")
    @Operation(
            method = "GET",
            summary = "Get all permissions of a role by id",
            description = "Get all permissions of a role by id"
    )
    @GetMapping(path = "/roles/{id}/permissions")
    public ResponseEntity<List<PermissionDTO>> getPermissionsOfRole(@PathVariable Long id) {
        log.info("Request for permissions of role by id {}", id);
        return new ResponseEntity<>(service.findPermissionsById(id), HttpStatus.OK);
    }

    @Tag(name = "Roles")
    @Operation(
            method = "POST",
            summary = "add permission to a role by id",
            description = "add permission to a role by id"
    )
    @PostMapping(path = "/roles/{id}/permissions")
    public ResponseEntity<Object> addPermissionToRole(@PathVariable Long id, @RequestBody Long permissionId) {
        log.info("Request to add permissions {} to a role by id: {}", permissionId, id);
        service.addPermissionToRole(id, permissionId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Tag(name = "Roles")
    @Operation(
            method = "DELETE",
            summary = "delete permissions to a role by id",
            description = "delete permissions to a role by id"
    )
    @DeleteMapping(path = "/roles/{id}/permissions/{permissionId}")
    public ResponseEntity<Object> deletePermissionToRole(@PathVariable Long id, @PathVariable Long permissionId) {
        log.info("Request to delete permission {} of a role by id: {}", permissionId, id);
        service.deletePermissionToRole(id, permissionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Tag(name = "Roles")
    @Operation(
            method = "GET",
            summary = "Get all roles",
            description = "Get all roles"
    )
    @GetMapping(path = "/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        log.info("Request to get all roles");
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @Tag(name = "Roles")
    @Operation(
            method = "PUT",
            summary = "Update role by id",
            description = "Update role by id"
    )
    @PutMapping(path = "/roles/{id}")
    public ResponseEntity<RoleDTO> update(@PathVariable Long id, @RequestBody RoleDTO roleRequestDTO){
        log.info("Updating role {} with role {} description {}", id, roleRequestDTO.getRole(), roleRequestDTO.getDescription());
        RoleDTO updatedRole = service.update(id, roleRequestDTO);
        return new ResponseEntity<>(updatedRole, HttpStatus.OK);
    }

    @Tag(name = "Roles")
    @Operation(
            method = "DELETE",
            summary = "delete role by id",
            description = "delete role by id"
    )
    @DeleteMapping(path = "/roles/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        log.info("Request to delete role by id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
