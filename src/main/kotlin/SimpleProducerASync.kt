import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.*

class SimpleProducerASync {

    companion object {

        private val logger = KotlinLogging.logger { }

        @JvmStatic
        fun main(args: Array<String>) {
            val props = initProducerProps()

            val topic = "simple-topic"

            val producer = KafkaProducer<String, String>(props)
            val record = ProducerRecord<String, String>(topic, "hello world 3")

            producer.send(record) { metadata, exception ->
                if (exception == null) {
                    logger.info {
                        """###### record metadata received #####
                                        |topic: ${metadata?.topic()}
                                        |partition: ${metadata?.partition()}
                                        |offset: ${metadata?.offset()}
                                        |timestamp: ${metadata?.timestamp()}
                                    """.trimMargin()
                    }
                } else {
                    logger.error { "exception error from broker ${exception.message}" }
                }
            }

            Thread.sleep(3000L)

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
