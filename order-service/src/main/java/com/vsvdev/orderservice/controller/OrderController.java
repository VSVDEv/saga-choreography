package com.vsvdev.orderservice.controller;

import com.vsvdev.dto.OrderRequestDto;
import com.vsvdev.orderservice.entity.PurchaseOrder;
import com.vsvdev.orderservice.service.OrderCommandService;
import com.vsvdev.orderservice.service.OrderQueryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {

  @Autowired
  private OrderCommandService commandService;

  @Autowired
  private OrderQueryService queryService;

  @PostMapping("/create")
  public PurchaseOrder createOrder(@RequestBody OrderRequestDto requestDTO){
    requestDTO.setOrderId(UUID.randomUUID());
    return this.commandService.createOrder(requestDTO);
  }

  @GetMapping("/all")
  public List<PurchaseOrder> getOrders(){
    return this.queryService.getAll();
  }

}
