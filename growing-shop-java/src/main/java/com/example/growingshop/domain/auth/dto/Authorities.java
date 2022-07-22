package com.example.growingshop.domain.auth.dto;

import java.util.ArrayList;
import java.util.List;

public class Authorities {
    private List<Authority> authorities = new ArrayList<>();

    public Authorities(List<Authority> authorities) {
        this.authorities.addAll(authorities);
    }

    public boolean isAllowAccessPath(String path) {
        return authorities.stream()
                .anyMatch(authority -> authority.isAllowAccessPath(path));
    }
}
