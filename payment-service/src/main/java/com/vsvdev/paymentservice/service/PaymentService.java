package com.vsvdev.paymentservice.service;

import com.vsvdev.dto.PaymentDto;
import com.vsvdev.events.order.OrderEvent;
import com.vsvdev.events.payment.PaymentEvent;
import com.vsvdev.events.payment.PaymentStatus;
import com.vsvdev.paymentservice.entity.UserTransaction;
import com.vsvdev.paymentservice.repository.UserBalanceRepository;
import com.vsvdev.paymentservice.repository.UserTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

  @Autowired
  private UserBalanceRepository balanceRepository;

  @Autowired
  private UserTransactionRepository transactionRepository;

  @Transactional
  public PaymentEvent newOrderEvent(OrderEvent orderEvent){
    var purchaseOrder = orderEvent.getPurchaseOrder();
    var dto = PaymentDto.of(purchaseOrder.getOrderId(), purchaseOrder.getUserId(), purchaseOrder.getPrice());
    return this.balanceRepository.findById(purchaseOrder.getUserId())
        .filter(ub -> ub.getBalance() >= purchaseOrder.getPrice())
        .map(ub -> {
          ub.setBalance(ub.getBalance() - purchaseOrder.getPrice());
          this.transactionRepository.save(UserTransaction.of(purchaseOrder.getOrderId(), purchaseOrder.getUserId(), purchaseOrder.getPrice()));
          return new PaymentEvent(dto, PaymentStatus.RESERVED);
        })
        .orElse(new PaymentEvent(dto, PaymentStatus.REJECTED));
  }

  @Transactional
  public void cancelOrderEvent(OrderEvent orderEvent){
    this.transactionRepository.findById(orderEvent.getPurchaseOrder().getOrderId())
        .ifPresent(ut -> {
          this.transactionRepository.delete(ut);
          this.balanceRepository.findById(ut.getUserId())
              .ifPresent(ub -> ub.setBalance(ub.getBalance() + ut.getAmount()));
        });
  }
}
