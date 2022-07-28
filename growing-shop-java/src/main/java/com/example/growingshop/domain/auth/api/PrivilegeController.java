package com.example.growingshop.domain.auth.api;

import com.example.growingshop.domain.auth.dto.RoleRequest;
import com.example.growingshop.domain.auth.dto.RoleResponse;
import com.example.growingshop.domain.auth.service.PrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/privileges")
@RequiredArgsConstructor
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    @PostMapping
    public ResponseEntity<RoleResponse.PrivilegeRes> createPrivilege(@RequestBody RoleRequest.CreatePrivilege req) {
        return new ResponseEntity<>(
                RoleResponse.PrivilegeRes.from(privilegeService.create(req)),
                HttpStatus.OK
        );
    }
}
