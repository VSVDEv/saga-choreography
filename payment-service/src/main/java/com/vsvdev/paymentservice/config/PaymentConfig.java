package com.vsvdev.paymentservice.config;

import com.vsvdev.events.order.OrderEvent;
import com.vsvdev.events.order.OrderStatus;
import com.vsvdev.events.payment.PaymentEvent;
import com.vsvdev.paymentservice.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class PaymentConfig {

  @Autowired
  private PaymentService service;

  @Bean
  public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
    return flux -> flux.flatMap(this::processPayment);
  }

  private Mono<PaymentEvent> processPayment(OrderEvent event){
    if(event.getOrderStatus().equals(OrderStatus.ORDER_CREATED)){
      return Mono.fromSupplier(() -> this.service.newOrderEvent(event));
    }else{
      return Mono.fromRunnable(() -> this.service.cancelOrderEvent(event));
    }
  }

}