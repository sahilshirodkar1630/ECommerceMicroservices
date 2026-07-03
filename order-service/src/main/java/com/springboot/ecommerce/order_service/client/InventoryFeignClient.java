package com.springboot.ecommerce.order_service.client;

import com.springboot.ecommerce.order_service.dto.OrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service",path = "/inventory")
public interface InventoryFeignClient {

    @PutMapping("/products/reduceStocks")
    public Double reduceStocks(@RequestBody OrderRequestDto orderRequestDto);


}
