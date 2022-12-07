package com.example.growingshoporder.dto;

import com.example.growingshoporder.domain.Order;
import com.example.growingshoporder.domain.OrderLine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OrderReq {
        private long id;
        private List<OrderRequest.OrderLineReq> orderLines = new ArrayList<>();

        public Order toEntity() {
            List<OrderLine> orderLines = this.orderLines.stream()
                    .map(OrderLineReq::toEntity)
                    .collect(Collectors.toList());

            return Order.builder()
                    .orderAt(LocalDateTime.now())
                    .orderLines(orderLines)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OrderLineReq {
        private Long id;
        private Long productId;
        private Integer price;
        private Integer quantity;

        public OrderLine toEntity() {
            return new OrderLine(productId, price, quantity);
        }
    }
}
