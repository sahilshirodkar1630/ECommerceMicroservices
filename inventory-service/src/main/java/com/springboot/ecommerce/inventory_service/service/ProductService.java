package com.springboot.ecommerce.inventory_service.service;


import com.springboot.ecommerce.inventory_service.dto.OrderRequestDto;
import com.springboot.ecommerce.inventory_service.dto.OrderRequestItemDto;
import com.springboot.ecommerce.inventory_service.dto.ProductDto;
import com.springboot.ecommerce.inventory_service.entity.Product;
import com.springboot.ecommerce.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public List<ProductDto> getAllInventory(){
        log.info("Fetching all inventory items");
        List<Product> inventories = productRepository.findAll();
        return inventories.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();

    }

    public ProductDto getProductById(Long id){
        log.info("Fetching Product with ID:{}",id);
        Optional<Product> inventory = productRepository.findById(id);
        return inventory.map(item -> modelMapper.map(item, ProductDto.class))
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }


    @Transactional
    public Double reduceStocks(OrderRequestDto orderRequestDto) {

        log.info("Reducing the stocks");
        Double totalAmount = 0.0;
        for(OrderRequestItemDto item : orderRequestDto.getItems()){
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found "+ productId));

            if(product.getStock() < quantity){
                throw new RuntimeException(" Product cannot be fulfilled for given quantity");
            }

            product.setStock(product.getStock()-quantity);
            productRepository.save(product);
            totalAmount += quantity*product.getPrice();

        }
        return totalAmount;
    }
}
