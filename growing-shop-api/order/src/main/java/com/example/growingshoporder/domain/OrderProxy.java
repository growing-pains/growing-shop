package com.example.growingshoporder.domain;

import com.example.domain.order.Order;
import com.example.domain.order.OrderLine;
import com.example.growingshoporder.dto.OrderRequest;
import com.example.growingshoporder.dto.OrderResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderProxy {

    private final Order order;

    public Long totalPrice() {
        return order.getOrderLines()
                .stream()
                .mapToLong(OrderLine::amounts)
                .sum();
    }

    public void delete() {
        order.delete();
    }

    public void upsertOrderLines(List<OrderRequest.OrderLineReq> req) {
        // TODO - 도메인 모듈을 업데이트 할 좋은 형태 방법 찾기
        // 도메인을 모듈화를 할 경우, 이와 같이 주문 모듈에서 상품을 업데이트하는 등의 행동을 하기가 어려움
//        order.getOrderLines().forEach(orderLine ->
//                req.stream()
//                        .filter(orderLineReq -> orderLineReq.getId().equals(orderLine.getId())).findFirst()
//                        .ifPresent(orderLineReq -> {
//                            orderLine.
//                        })
//        );
        order.getOrderLines().stream()
                .filter(line -> req.stream().noneMatch(orderLineReq -> orderLineReq.getId().equals(line.getId())))
                .forEach(OrderLine::delete);
        req.stream()
                .filter(orderLineReq -> orderLineReq.getId() == null)
                .forEach(orderLineReq -> order.getOrderLines().add(orderLineReq.toEntity()));
    }

    public List<OrderResponse.OrderLineRes> convertOrderLineRes() {
        return order.getOrderLines().stream()
                .map(OrderResponse.OrderLineRes::from)
                .collect(Collectors.toList());
    }

    public Order value() {
        return order;
    }
}
