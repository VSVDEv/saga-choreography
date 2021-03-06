package com.vsvdev.orderservice.config;


import com.vsvdev.events.order.OrderEvent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class OrderConfig {

  @Bean
  public Sinks.Many<OrderEvent> orderSink(){
    return Sinks.many().unicast().onBackpressureBuffer();
  }

  @Bean
  public Supplier<Flux<OrderEvent>> orderSupplier(Sinks.Many<OrderEvent> sink){
    return sink::asFlux;
  }

}
