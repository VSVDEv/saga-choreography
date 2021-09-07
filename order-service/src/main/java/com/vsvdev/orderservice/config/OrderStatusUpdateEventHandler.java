package com.vsvdev.orderservice.config;

import com.vsvdev.events.inventory.InventoryStatus;
import com.vsvdev.events.order.OrderStatus;
import com.vsvdev.events.payment.PaymentStatus;
import com.vsvdev.orderservice.entity.PurchaseOrder;
import com.vsvdev.orderservice.repository.PurchaseOrderRepository;
import com.vsvdev.orderservice.service.OrderStatusPublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class OrderStatusUpdateEventHandler {

  @Autowired
  private PurchaseOrderRepository repository;

  @Autowired
  private OrderStatusPublisher publisher;

  @Transactional
  public void updateOrder(final UUID id, Consumer<PurchaseOrder> consumer){
    this.repository
        .findById(id)
        .ifPresent(consumer.andThen(this::updateOrder));

  }

  private void updateOrder(PurchaseOrder purchaseOrder){
    if(Objects.isNull(purchaseOrder.getInventoryStatus()) || Objects.isNull(purchaseOrder.getPaymentStatus()))
      return;
    var isComplete = PaymentStatus.RESERVED.equals(purchaseOrder.getPaymentStatus()) && InventoryStatus.RESERVED.equals(purchaseOrder.getInventoryStatus());
    var orderStatus = isComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
    purchaseOrder.setOrderStatus(orderStatus);
    if (!isComplete){
      this.publisher.raiseOrderEvent(purchaseOrder, orderStatus);
    }
  }

}
