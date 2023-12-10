package com.tutorial.cdc.product.service;

import com.tutorial.cdc.product.entity.Product;
import com.tutorial.cdc.product.repository.ProductRepository;
import io.debezium.data.Envelope;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void handleEvent(String operation, String documentId, String collection, Product product) {

        // Check if the operation is either CREATE or READ
        if (Envelope.Operation.CREATE.code().equals(operation) || Envelope.Operation.READ.code().equals(operation)) {
            // Set the MongoDB document ID to the product
            product.setMongoId(documentId);
            product.setSourceCollection(collection);
            // Save the updated product information to the database
            productRepository.save(product);

            // If the operation is DELETE
        } else if(Envelope.Operation.UPDATE.code().equals(operation)){
           var productToUpdate = productRepository.findByMongoId(documentId);
            product.setId(productToUpdate.getId());
            product.setMongoId(documentId);
            product.setSourceCollection(collection);
            productRepository.save(product);
        }
        // If the operation is DELETE
        else if (Envelope.Operation.DELETE.code().equals(operation)) {
            // Remove the product from the database using the MongoDB document ID
            productRepository.removeProductByMongoId(documentId);
        }
    }
}
