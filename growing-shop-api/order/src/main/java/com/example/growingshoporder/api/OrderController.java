package com.example.growingshoporder.api;

import com.example.growingshoporder.dto.OrderRequest;
import com.example.growingshoporder.dto.OrderResponse;
import com.example.growingshoporder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse.OrderRes>> findAll() {
        return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse.OrderRes> findById(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderResponse.OrderRes> create(@RequestBody OrderRequest.OrderReq req) {
        return new ResponseEntity<>(orderService.order(req), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/orderLines")
    public ResponseEntity<List<OrderResponse.OrderLineRes>> upsert(@PathVariable Long id, @RequestBody List<OrderRequest.OrderLineReq> req) {
        return new ResponseEntity<>(orderService.upsertOrderLines(id, req), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        orderService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/orderLines")
    public ResponseEntity<Object> deleteOrderLines(List<Long> orderLineIds) {
        orderService.deleteOrderLines(orderLineIds);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
