package com.example.growingshop.domain.auth.api;

import com.example.growingshop.domain.auth.dto.RoleRequest;
import com.example.growingshop.domain.auth.dto.RoleResponse;
import com.example.growingshop.domain.auth.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
public class PolicyController {
    private final PolicyService policyService;

    @PostMapping
    public ResponseEntity<RoleResponse.PoliciesRes> create(@RequestBody RoleRequest.CreatePolicy req) {
        return new ResponseEntity<>(
                RoleResponse.PoliciesRes.from(policyService.create(req)),
                HttpStatus.OK
        );
    }

    @DeleteMapping
    public void delete(Long id) {
        policyService.delete(id);
    }
}
