package com.example.growingshop.domain.order.dto;

import com.example.growingshop.domain.order.domain.Order;
import com.example.growingshop.domain.order.domain.OrderLine;
import com.example.growingshop.domain.product.dto.ProductResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OrderRes {
        private long id;
        private LocalDateTime orderAt;
        private List<OrderLineRes> orderLines = new ArrayList<>();
        private long totalPrice;

        public static OrderRes from(Order order) {
            OrderRes res = new OrderRes();

            res.id = order.getId();
            res.orderAt = order.getOrderAt();
            res.orderLines.addAll(
                    order.getOrderLines()
                            .stream()
                            .map(OrderLineRes::from)
                            .collect(Collectors.toList())
            );
            res.totalPrice = order.totalPrice();

            return res;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OrderLineRes {
        private long id;
        private ProductResponse.ProductRes product;
        private int price;
        private int quantity;

        public static OrderLineRes from(OrderLine orderLine) {
            OrderLineRes res = new OrderLineRes();

            res.id = orderLine.getId();
//            res.product = ProductResponse.ProductRes.from(orderLine.getProductId());
            // FIXME - 의존성 제거 후 상품 정보 넣는 로직 추가
            res.price = orderLine.getPrice();
            res.quantity = orderLine.getQuantity();

            return res;
        }
    }
}
