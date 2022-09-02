package com.example.growingshop.domain.auth.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Policy {
    private static final Pattern PATH_REGEX = Pattern.compile("^[\\/]+[\\w-/]+$", Pattern.DOTALL);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String path;

    private String description;

    @Enumerated(EnumType.STRING)
    private HttpMethod method;

    private Policy(Long id, String name, String path, String description) {
        validatePath(path);

        this.id = id;
        this.name = name;
        this.path = path;
        this.description = description;
    }

    public Policy(String name, String path, String description) {
        this(null, name, path, description);
    }

    private static void validatePath(String path) {
        if (!PATH_REGEX.matcher(path).matches()) {
            throw new IllegalArgumentException("유효하지 않은 url path 입니다.");
        }
    }
}
