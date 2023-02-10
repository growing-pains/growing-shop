package com.example.growingshoporder.dto;

import com.example.domain.order.Order;
import com.example.domain.order.OrderLine;
import com.example.domain.order.OrderStatus;
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

            return new Order(null, OrderStatus.WAITING, LocalDateTime.now(), orderLines);
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
//            TODO - 도메인 모듈의 orderLine 은 Product 엔티티를 직접 연동되어 있음. 이러한 형태를 풀어낼 수 있는 방법 찾기
//            return new OrderLine(productId, price, quantity);
            return new OrderLine();
        }
    }
}
