import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class SimpleProducerSync {

    companion object {

        //        val logger: Logger = LoggerFactory.getLogger(SimpleProducerSync::class.java.name)
        val logger = KotlinLogging.logger { }

        @JvmStatic
        fun main(args: Array<String>) {
            val props = initProducerProps()

            val topic = "simple-topic"

            val producer = KafkaProducer<String, String>(props)
            val record = ProducerRecord<String, String>(topic, "hello world 3")

            val recordMetadata = producer.send(record).get()
            logger.info {
                """###### record metadata received #####
                    |topic: ${recordMetadata.topic()}
                    |partition: ${recordMetadata.partition()}
                    |offset: ${recordMetadata.offset()}
                    |timestamp: ${recordMetadata.timestamp()}
                """.trimMargin()
            }
            producer.close()
        }

        private fun initProducerProps(): Properties {
            val props = Properties().apply {
                // bootstrap.servers, key.serializer.class, value.serializer.class
                put(BOOTSTRAP_SERVERS_CONFIG, "10.211.55.53:9092")
                put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
                put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            }
            return props
        }
    }

}
