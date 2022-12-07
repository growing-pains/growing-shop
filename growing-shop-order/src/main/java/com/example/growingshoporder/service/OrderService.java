package com.example.growingshoporder.service;

import com.example.growingshoporder.domain.Order;
import com.example.growingshoporder.dto.OrderRequest;
import com.example.growingshoporder.dto.OrderResponse;
import com.example.growingshoporder.repository.OrderLineRepository;
import com.example.growingshoporder.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    public List<OrderResponse.OrderRes> findAll() {
//        User loginUser = LoginUser.getUserInSecurityContext();
//
//        if (loginUser.getType() == UserType.ADMIN) {
//            return OrderResponse.OrderRes
//                    .from(orderRepository.findAllNotDeleted());
//        }
//
//        return OrderResponse.OrderRes
//                .from(orderRepository.findAllNotDeletedByUserId(loginUser.getId()));
        return null;
    }

    public OrderResponse.OrderRes getById(Long id) {
//        User loginUser = LoginUser.getUserInSecurityContext();
//
//        if (loginUser.getType() == UserType.ADMIN) {
//            return OrderResponse.OrderRes
//                    .from(getOne(id));
//        }
//
//        Order order = orderRepository.findNotDeletedByIdAndUserId(id, loginUser.getId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
//
//        return OrderResponse.OrderRes
//                .from(order);
        return null;
    }

    @Transactional
    public OrderResponse.OrderRes order(OrderRequest.OrderReq req) {
        return OrderResponse.OrderRes.from(
                orderRepository.save(req.toEntity())
        );
    }

    @Transactional
    public List<OrderResponse.OrderLineRes> upsertOrderLines(Long id, List<OrderRequest.OrderLineReq> req) {
        Order order = getOne(id);
        order.upsertOrderLines(req);

        return order.getOrderLines().stream()
                .map(OrderResponse.OrderLineRes::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        getOne(id).delete();
    }

    @Transactional
    public void deleteOrderLines(List<Long> orderLineIds) {
        if (orderLineIds.isEmpty()) {
            throw new IllegalArgumentException("입력된 id 값이 없습니다.");
        }

        orderLineRepository.deleteAll(orderLineRepository.findAllById(orderLineIds));
    }

    private Order getOne(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id + " 에 해당하는 주문이 존재하지 않습니다."));
    }
}
