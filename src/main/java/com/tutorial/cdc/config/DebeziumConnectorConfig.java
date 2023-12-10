package com.tutorial.cdc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DebeziumConnectorConfig {

  @Value("${spring.data.mongodb.uri}")
  private String mongoDbUri;

  @Value("${spring.data.mongodb.username:}")
  private String mongoDbUsername;

  @Value("${spring.data.mongodb.password:}")
  private String mongoDbPassword;

  @Value("${spring.datasource.url}")
  private String postgresUrl;

  @Value("${spring.datasource.username}")
  private String postgresUsername;

  @Value("${spring.datasource.password}")
  private String postgresPassword;

  @Value("${database.include.list}")
  private String databaseIncludeList;

  @Value("${collection.include.list}")
  private String collectionIncludeList;

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Bean
  public io.debezium.config.Configuration mongodbConnector() {

    Map<String, String> configMap = new HashMap<>();

    //This sets the name of the Debezium connector instance. It’s used for logging and metrics.
    configMap.put("name", "cdc-mongodb");
    //This specifies the Java class for the connector. Debezium uses this to create the connector instance.
    configMap.put("connector.class", "io.debezium.connector.mongodb.MongoDbConnector");
    //This sets the Java class that Debezium uses to store the progress of the connector.
    // In this case, it’s using a JDBC-based store, which means it will store the progress in a relational database.
    configMap.put("offset.storage", "io.debezium.storage.jdbc.offset.JdbcOffsetBackingStore");
    //This is the JDBC URL for the database where Debezium stores the connector offsets (progress).
    configMap.put("offset.storage.jdbc.url", postgresUrl);
    configMap.put("offset.storage.jdbc.user", postgresUsername);
    configMap.put("offset.storage.jdbc.password", postgresPassword);
    //This is the MongoDB connection string that Debezium uses to connect to your MongoDB instance
    configMap.put("mongodb.connection.string", mongoDbUri);
    //This prefix is added to all Kafka topics that this connector writes to.
    configMap.put("topic.prefix", "sbd-mongodb-connector");
    //This is a comma-separated list of MongoDB database names that the connector will monitor for changes.
    configMap.put("database.include.list", databaseIncludeList);
    //This is a comma-separated list of MongoDB collection names that the connector will monitor for changes.
    configMap.put("collection.include.list", collectionIncludeList);
    //When errors.log.include.messages set to true, then any error messages resulting from failed operations
    // are also written to the log.
    configMap.put("errors.log.include.messages", "true");

    //Use MongoDB without credentials and without SSL for local and test profiles
    if (!"local".equals(activeProfile) && !"test".equals(activeProfile)) {
      configMap.put("mongodb.user", mongoDbUsername);
      configMap.put("mongodb.password", mongoDbPassword);
      configMap.put("mongodb.ssl.enabled", "true");
    }

    return io.debezium.config.Configuration.from(configMap);
  }
}
