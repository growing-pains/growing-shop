package com.example.growingshop.domain.auth.api;

import com.example.growingshop.domain.auth.dto.RoleRequest;
import com.example.growingshop.domain.auth.dto.RoleResponse;
import com.example.growingshop.domain.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleResponse.RoleRes>> findAll() {
        return new ResponseEntity<>(roleService.findAll().toResponse(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RoleResponse.RoleRes> createRole(@RequestBody RoleRequest.CreateRole req) {
        return new ResponseEntity<>(
                RoleResponse.RoleRes.from(roleService.create(req)),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/privileges")
    public ResponseEntity changePrivileges(@RequestBody RoleRequest.changeRolePrivileges req) {
        roleService.changePrivileges(req);

        return ResponseEntity.noContent().build();
    }
}
