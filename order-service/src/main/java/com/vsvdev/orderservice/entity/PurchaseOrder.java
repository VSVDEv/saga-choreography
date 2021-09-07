package com.vsvdev.orderservice.entity;

import com.vsvdev.events.inventory.InventoryStatus;
import com.vsvdev.events.order.OrderStatus;
import com.vsvdev.events.payment.PaymentStatus;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@Data
@Entity
@ToString
public class PurchaseOrder {

  @Id
  private UUID id;
  private Integer userId;
  private Integer productId;
  private Integer price;
  private OrderStatus orderStatus;
  private PaymentStatus paymentStatus;
  private InventoryStatus inventoryStatus;

  @Version
  private int version;

}
