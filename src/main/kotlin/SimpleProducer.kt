import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

class SimpleProducer(
) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val props = initProducerProps()

            val topic = "simple-topic"

            val producer = KafkaProducer<String, String>(props)
            val record = ProducerRecord<String, String>(topic, "hello world 2")

            producer.send(record)
            producer.flush()
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
