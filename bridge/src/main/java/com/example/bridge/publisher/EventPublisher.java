package com.example.bridge.publisher;

import com.example.bridge.config.MessagingConfig;
import com.example.bridge.dtos.requests.UpdateEventRequest;
import com.example.bridge.dtos.requests.CreateEventRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event/order")
public class EventPublisher {

    private final RabbitTemplate template;

    public EventPublisher(RabbitTemplate template) {
        this.template = template;
    }


    @PostMapping("/create")
    public void sendCreateEvent(@RequestBody CreateEventRequest request){
        template.convertAndSend(MessagingConfig.EXCHANGE, "order_received", request);
    }


    @PostMapping("/start")
    public void sendStartDeliveryEvent(@RequestBody UpdateEventRequest request) {

        template.convertAndSend(MessagingConfig.EXCHANGE, "delivery_started", request);

    }


    @PostMapping("/finish")
    public void sendFinishStartDelivery(@RequestBody UpdateEventRequest request){

        template.convertAndSend(MessagingConfig.EXCHANGE, "order_finished", request);
    }


    
}
