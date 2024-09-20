import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.InvalidRecordException
import org.apache.kafka.common.utils.Utils

class CustomPartitioner : Partitioner {

    private lateinit var specialKey: String

    private val logger = KotlinLogging.logger { }

    override fun configure(configs: MutableMap<String, *>?) {
        specialKey = configs?.get("custom.specialKey").toString()
    }

    override fun partition(
        topic: String?,
        key: Any?,
        keyBytes: ByteArray?,
        value: Any?,
        valueBytes: ByteArray?,
        cluster: Cluster
    ): Int {
        if (keyBytes == null)
            throw InvalidRecordException("key should not be null")

        val partitionInfoMutableList = cluster.partitionsForTopic(topic)
        val numPartitions = partitionInfoMutableList.size
        val numSpecialPartitions = (numPartitions * 0.5).toInt()
        val partitionIndex = if (key == specialKey) {
            // 0, 1
            Utils.toPositive(Utils.murmur2(valueBytes)) % numSpecialPartitions
        }
        else {
            // 2, 3, 4, ...
            Utils.toPositive(Utils.murmur2(keyBytes)) % (numPartitions - numSpecialPartitions) + numSpecialPartitions
        }
        logger.info { "key: $key is sent to partition: $partitionIndex" }

        return partitionIndex
    }

    override fun close() {
        logger.info { "closed custom partitioner" }
    }
}
