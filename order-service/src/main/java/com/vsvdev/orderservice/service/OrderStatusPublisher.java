package com.vsvdev.orderservice.service;


import com.vsvdev.dto.PurchaseOrderDto;
import com.vsvdev.events.order.OrderEvent;
import com.vsvdev.events.order.OrderStatus;
import com.vsvdev.orderservice.entity.PurchaseOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisher {

  @Autowired
  private Sinks.Many<OrderEvent> orderSink;

  public void raiseOrderEvent(final PurchaseOrder purchaseOrder, OrderStatus orderStatus){
    var dto = PurchaseOrderDto.of(
        purchaseOrder.getId(),
        purchaseOrder.getProductId(),
        purchaseOrder.getPrice(),
        purchaseOrder.getUserId()
    );
    var orderEvent = new OrderEvent(dto, orderStatus);
    this.orderSink.tryEmitNext(orderEvent);
  }

}
