package com.dpm.pumping.common.driver

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase

@Component
class Mongo(
    @Value("\${mongodb.databaseName}")
    private val databaseName: String,

    @Value("\${mongodb.mongoUri}")
    private val mongoUri: String,

    @Value("\${mongodb.username}")
    private val username: String,

    @Value("\${mongodb.password}")
    private val password: String
) {
    private val mongoClient: MongoClient = MongoClients.create(
        MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(mongoUri))
            .credential(MongoCredential.createCredential(username, databaseName, password.toCharArray()))
            .build()
    )
    val database: MongoDatabase = mongoClient.getDatabase(databaseName)
}

