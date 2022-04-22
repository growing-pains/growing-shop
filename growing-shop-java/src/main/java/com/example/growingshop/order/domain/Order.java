package com.example.growingshop.order.domain;

import com.example.growingshop.user.domain.UserId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "`order`")
public class Order {
    @EmbeddedId
    private OrderId id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime orderAt;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "id", column = @Column(name = "user")))
    private UserId user;

    @ElementCollection
    @CollectionTable(name = "order_line", joinColumns = {@JoinColumn(name = "`order`")})
    private List<OrderLine> orderLines = new ArrayList<>();

    public Long totalAmounts() {
        return orderLines.stream().mapToLong(OrderLine::amounts).sum();
    }
}
