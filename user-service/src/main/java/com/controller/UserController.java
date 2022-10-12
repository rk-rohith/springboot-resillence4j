package com.controller;

import com.model.OrderDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user-service")
public class UserController {

    @Lazy
    @Autowired
    private RestTemplate restTemplate;

    private static final String BASEURL = "http://localhost:9191/orders";
    private static final String USER_SERVICE = "userService";

    @GetMapping("/displayOrders")
    @CircuitBreaker(name = USER_SERVICE, fallbackMethod = "getAllAvailableProducts")
    public List<OrderDTO> displayOrders(@RequestParam("category") String category) {
        String url = category == null ? BASEURL : BASEURL + "/" + category;
        return restTemplate.getForObject(url, ArrayList.class);
    }

    public List<OrderDTO> getAllAvailableProducts(Exception ex) {
        return Arrays.asList(new OrderDTO(119, "mobile", "electronics", "white", 20000),
                new OrderDTO(565, "T-Shirt", "clothes", "black", 999),
                new OrderDTO(787, "Jeans", "clothes", "blue", 1999),
                new OrderDTO(909, "Laptop", "electronics", "gray", 50000),
                new OrderDTO(200, "digital watch", "electronics", "black", 2500),
                new OrderDTO(456, "Fan", "electronics", "black", 50000));
    }
}
