package com.example.growingshop.domain.auth.domain;

import lombok.*;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Privilege {
    private static final Pattern PATH_REGEX = Pattern.compile("^([\\/\\w-]+)+(\\.){0,1}$", Pattern.DOTALL);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String path;

    private String description;

    public Privilege(String name, String path, String description) {
        validatePath(path);

        this.name = name;
        this.path = path;
        this.description = description;
    }

    private void validatePath(String path) {
        if (!PATH_REGEX.matcher(path).matches()) {
            throw new IllegalArgumentException("유효하지 않은 url path 입니다.");
        }
    }
}
