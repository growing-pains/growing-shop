package com.example.growingshop.domain.order.service;

import com.example.growingshop.domain.auth.accessible.LoginUser;
import com.example.growingshop.domain.order.domain.Order;
import com.example.growingshop.domain.order.domain.OrderLine;
import com.example.growingshop.domain.order.dto.OrderRequest;
import com.example.growingshop.domain.order.dto.OrderResponse;
import com.example.growingshop.domain.order.repository.OrderLineRepository;
import com.example.growingshop.domain.order.repository.OrderRepository;
import com.example.growingshop.domain.user.domain.User;
import com.example.growingshop.domain.user.domain.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    public List<OrderResponse.OrderRes> findAll() {
        User loginUser = LoginUser.getUserInSecurityContext();

//        if (loginUser.getType() == UserType.ADMIN) {
//            return OrderResponse.OrderRes
//                    .from(orderRepository.findAllNotDeleted());
//        }

        return OrderResponse.OrderRes
                .from(orderRepository.findAllByUserId(loginUser.getId()));
    }

    public OrderResponse.OrderRes getById(Long id) {
        User loginUser = LoginUser.getUserInSecurityContext();

        if (loginUser.getType() == UserType.ADMIN) {
            return OrderResponse.OrderRes
                    .from(getOne(id));
        }

        Order order = orderRepository.findByIdAndUserId(id, loginUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        return OrderResponse.OrderRes
                .from(order);
    }

    @Transactional
    public OrderResponse.OrderRes order(OrderRequest.OrderReq req) {
        return OrderResponse.OrderRes.from(
                orderRepository.save(req.toEntity())
        );
    }

    @Transactional
    public List<OrderResponse.OrderLineRes> upsertOrderLines(List<OrderRequest.OrderLineReq> req) {
        List<OrderLine> updateOrderLines = req.stream()
                .filter(orderLineReq -> orderLineReq.getId() != null)
                .map(OrderRequest.OrderLineReq::toEntity)
                .collect(Collectors.toList());
        List<OrderLine> insertOrderLines = req.stream()
                .filter(orderLineReq -> orderLineReq.getId() == null)
                .map(OrderRequest.OrderLineReq::toEntity)
                .collect(Collectors.toList());

        orderLineRepository.saveAll(updateOrderLines);
        orderLineRepository.saveAll(insertOrderLines);

        return Stream.concat(updateOrderLines.stream(), insertOrderLines.stream())
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
