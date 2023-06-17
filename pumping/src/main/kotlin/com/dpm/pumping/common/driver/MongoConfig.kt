import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoConfig(
    @Value("\${mongodb.database}")
    private val databaseName: String,

    @Value("\${mongodb.uri}")
    private val mongoUri: String,

    @Value("\${mongodb.username}")
    private val username: String,

    @Value("\${mongodb.password}")
    private val password: String
) {

    @Bean
    fun mongoClient(): com.mongodb.client.MongoClient {
        val connectionString = ConnectionString(mongoUri)
        val credential = MongoCredential.createCredential(username, databaseName, password.toCharArray())
        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .credential(credential)
            .build()

        return MongoClients.create(settings)
    }
}
