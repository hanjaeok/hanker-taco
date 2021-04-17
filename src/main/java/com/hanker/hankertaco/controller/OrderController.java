package com.hanker.hankertaco.controller;

import com.hanker.hankertaco.domain.Order;
import com.hanker.hankertaco.domain.User;
import com.hanker.hankertaco.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @GetMapping("/current")
    public String orderForm(@AuthenticationPrincipal User user,
                            @ModelAttribute Order order){
        //model.addAttribute("order", new Order());
        if(order.getDeliveryName() == null){
            order.setDeliveryName(user.getFullname());
        }
        if(order.getDeliveryStreet() == null){
            order.setDeliveryStreet(user.getStreet());
        }
        if(order.getDeliveryCity() == null){
            order.setDeliveryCity(user.getCity());
        }
        if(order.getDeliveryState() == null){
            order.setDeliveryStreet(user.getState());
        }
        if(order.getDeliveryZip() == null){
            order.setDeliveryZip(user.getZip());
        }

        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus,
                               @AuthenticationPrincipal User user){

        if(errors.hasErrors()){
            return "orderForm";
        }
        //log.info("Order submitted : " + order);
        order.setUser(user);

        orderRepository.save(order);
        sessionStatus.setComplete();

        return "redirect:/";
    }
}
