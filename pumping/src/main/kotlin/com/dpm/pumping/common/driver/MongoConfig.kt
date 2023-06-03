package com.dpm.pumping.common.driver

import com.mongodb.MongoCredential
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoClientFactoryBean

@Configuration
class MongoConfig(
    @Value("\${mongodb.databaseName}")
    private val databaseName: String,

    @Value("\${mongodb.mongoUri}")
    private val mongoUri: String,

    @Value("\${mongodb.username}")
    private val username: String,

    @Value("\${mongodb.password}")
    private val password: String
) {

    @Bean
    fun mongo(): MongoClientFactoryBean {
        val mongo = MongoClientFactoryBean()
        mongo.setHost(mongoUri)
        mongo.setCredential(arrayOf(MongoCredential.createCredential(username, databaseName, password.toCharArray())))
        return mongo
    }
}
