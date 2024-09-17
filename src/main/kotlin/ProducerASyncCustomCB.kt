import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

class ProducerASyncCustomCB {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val props = initProducerProps(
                keySerClass = IntegerSerializer::class.java,
                valueSerClass = StringSerializer::class.java
            )

            val topic = "multipart-topic"

            val producer = KafkaProducer<Int, String>(props)

            for (seq in 0..<20) {
                val record =
                    ProducerRecord(topic, seq, "hello world $seq")
                producer.send(record, CustomCallback(seq))
            }

            Thread.sleep(3000L)

            producer.close()
        }

        private fun <K : Serializer<*>, V : Serializer<*>> initProducerProps(
            keySerClass: Class<K>,
            valueSerClass: Class<V>
        ): Properties {
            val props = Properties().apply {
                put(BOOTSTRAP_SERVERS_CONFIG, "10.211.55.53:9092")
                put(KEY_SERIALIZER_CLASS_CONFIG, keySerClass.name)
                put(VALUE_SERIALIZER_CLASS_CONFIG, valueSerClass.name)
            }
            return props
        }
    }
}
