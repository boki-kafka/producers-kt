import io.github.oshai.kotlinlogging.KotlinLogging
import net.datafaker.Faker
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.*
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

class PizzaProducerCustomPartitioner {

    companion object {

        private val logger = KotlinLogging.logger { }

        private fun sendPizzaMessage(
            producer: KafkaProducer<String, String>,
            topicName: String,
            iterCnt: Int,
            interIntervalMillis: Int,
            intervalMillis: Int,
            intervalCount: Int,
            isSync: Boolean
        ) {
            val seed: Long = 2024
            val random = Random(seed)
            val faker = Faker(random)
            var iterSeq = 0

            val startTime = System.currentTimeMillis()

            while (iterSeq++ != iterCnt) {
                val pMessage = PizzaMessage.produceMsg(faker, random, iterSeq)
                val record = ProducerRecord<String, String>(topicName, pMessage["key"], pMessage["message"])

                sendMessage(producer, record, pMessage, isSync)

                if ((intervalCount > 0 && (iterSeq % intervalCount) == 0)) {
                    logger.info { "###### IntervalCount: $intervalCount intervalMillis: $intervalMillis ######" }
                    Thread.sleep(intervalMillis.toLong())
                }

                if (interIntervalMillis > 0) {
                    logger.info { "interIntervalMillis: $interIntervalMillis" }
                    Thread.sleep(interIntervalMillis.toLong())
                }
            }
            val endTime = System.currentTimeMillis()
            val timeElapsed = endTime - startTime

            logger.info { "$timeElapsed millisecond elapsed for $iterCnt iterations" }
        }

        private fun sendMessage(
            producer: KafkaProducer<String, String>,
            record: ProducerRecord<String, String>,
            pMessage: Map<String, String>,
            isSync: Boolean
        ) {
            if (!isSync) {
                producer.send(record) { metadata, exception ->
                    if (exception == null) {
                        logger.info { "async message: ${pMessage["key"]} / partition: ${metadata.partition()} / offset: ${metadata.offset()}" }
                    } else {
                        logger.error { "exception error from broker: ${exception.message}" }
                    }
                }
            } else {
                val metadata = producer.send(record).get()
                logger.info { "sync message: ${pMessage["key"]} / partition: ${metadata.partition()} / offset: ${metadata.offset()}" }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val props = initProducerProps(
                keySerClass = StringSerializer::class.java,
                valueSerClass = StringSerializer::class.java
            )

            val topic = "pizza-topic-partitioner"

            val producer = KafkaProducer<String, String>(props)

            sendPizzaMessage(
                producer = producer,
                topicName = topic,
                iterCnt = -1,
                interIntervalMillis = 1000,
                intervalMillis = 0,
                intervalCount = 0,
                isSync = true
            )

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
                put(PARTITIONER_CLASS_CONFIG, CustomPartitioner::class.java)
                put("custom.specialKey", "P001")
            }
            return props
        }
    }
}
