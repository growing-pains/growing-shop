package com.example.growingshopauth.auth.api

import com.example.growingshopauth.auth.domain.toResponse
import com.example.growingshopauth.auth.dto.RoleRequest
import com.example.growingshopauth.auth.dto.RoleResponse
import com.example.growingshopauth.auth.service.RoleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/roles")
class RoleController(
    private val roleService: RoleService
) {

    @GetMapping
    fun findAll(): ResponseEntity<List<RoleResponse.RoleRes>> = ResponseEntity(
        roleService.findAll().toResponse(),
        HttpStatus.OK
    )

    @PostMapping
    fun create(@RequestBody req: RoleRequest.CreateRole): ResponseEntity<RoleResponse.RoleRes> = ResponseEntity(
        RoleResponse.RoleRes.from(roleService.create(req)),
        HttpStatus.CREATED
    )

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        roleService.delete(id)

        return ResponseEntity(HttpStatus.OK)
    }
}
