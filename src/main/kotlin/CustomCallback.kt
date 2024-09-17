import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.RecordMetadata

class CustomCallback(private val seq: Int) : Callback {
    private val logger = KotlinLogging.logger { }

    override fun onCompletion(rm: RecordMetadata, e: Exception?) {
        if (e == null) {
            logger.info {"seq:$seq, partition:${rm.partition()}, offset:${rm.offset()}"}
        }
        else {
            logger.error { "exception error from broker ${e.message}" }
        }
    }

}
