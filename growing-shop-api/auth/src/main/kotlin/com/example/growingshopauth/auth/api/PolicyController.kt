package com.example.growingshopauth.auth.api

import com.example.growingshopauth.auth.dto.RoleRequest
import com.example.growingshopauth.auth.dto.RoleResponse
import com.example.growingshopauth.auth.service.PolicyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/policies")
class PolicyController(
    private val policyService: PolicyService
) {

    @PostMapping
    fun create(@RequestBody req: RoleRequest.CreatePolicy): ResponseEntity<RoleResponse.PoliciesRes> = ResponseEntity(
        RoleResponse.PoliciesRes.from(policyService.create(req)),
        HttpStatus.OK
    )

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        policyService.delete(id)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
