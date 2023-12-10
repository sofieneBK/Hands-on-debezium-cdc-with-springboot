package com.tutorial.cdc.product.service;

import com.tutorial.cdc.product.entity.Product;

public interface ProductService {
    void handleEvent(String operation, String documentId, String collection, Product product);
}
