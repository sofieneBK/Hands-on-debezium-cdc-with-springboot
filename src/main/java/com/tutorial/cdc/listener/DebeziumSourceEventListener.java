package com.tutorial.cdc.listener;

import com.tutorial.cdc.product.entity.Product;
import com.tutorial.cdc.product.service.ProductService;
import com.tutorial.cdc.utils.HandlerUtils;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class DebeziumSourceEventListener {

    //This will be used to run the engine asynchronously
    private final Executor executor;

    //DebeziumEngine serves as an easy-to-use wrapper around any Debezium connector
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;

    //Inject product service
    private final ProductService productService;


    public DebeziumSourceEventListener(
            Configuration mongodbConnector, ProductService productService) {
        // Create a new single-threaded executor.
        this.executor = Executors.newSingleThreadExecutor();

        // Create a new DebeziumEngine instance.
        this.debeziumEngine =
                DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                        .using(mongodbConnector.asProperties())
                        .notifying(this::handleChangeEvent)
                        .build();

        // Set the product service.
        this.productService = productService;
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        Struct sourceRecordKey = (Struct) sourceRecord.key();
        Struct sourceRecordValue = (Struct) sourceRecord.value();
        if (sourceRecordValue != null) {
            try {

                String operation = HandlerUtils.getOperation(sourceRecordValue);

                String documentId = HandlerUtils.getDocumentId(sourceRecordKey);

                String collection = HandlerUtils.getCollection(sourceRecordValue);

                Product product = HandlerUtils.getData(sourceRecordValue);

                productService.handleEvent(operation, documentId, collection, product);

                log.info("Collection : {} , DocumentId : {} , Operation : {}", collection, documentId, operation);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }
}
