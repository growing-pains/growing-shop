package com.example.growingshop.order.domain;

import com.example.growingshop.user.domain.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "`order`")
public class Order {
    @EmbeddedId
    private OrderId id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime orderAt;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "user", nullable = false)))
    private UserId user;

    @ElementCollection
    @CollectionTable(name = "order_line", joinColumns = {@JoinColumn(name = "`order`")})
    @Valid
    @NotEmpty
    private List<OrderLine> orderLines = new ArrayList<>();

    public Long totalAmounts() {
        return orderLines.stream().mapToLong(OrderLine::amounts).sum();
    }

    @Builder
    public Order(LocalDateTime orderAt, UserId user, List<OrderLine> orderLines) {
        this.orderAt = orderAt;
        this.user = user;
        this.orderLines = orderLines;
    }
}
