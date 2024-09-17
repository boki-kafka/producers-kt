import net.datafaker.Faker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PizzaMessage {

    companion object {

//        private val pizzaNames: List<String> = listOf(
//            "Potato Pizza", "Cheese Pizza",
//            "Cheese Garlic Pizza", "Super Supreme", "Peperoni"
//        )

        // 피자 이름
        private val pizzaNames: List<String> = listOf(
            "고구마 피자", "치즈 피자",
            "치즈 갈릭 피자", "슈퍼 슈프림", "페페로니 피자"
        )

        // 피자 가게명
        private val pizzaShop: List<String> = listOf(
            "A001", "B001", "C001",
            "D001", "E001", "F001", "G001", "H001", "I001", "J001", "K001", "L001", "M001", "N001",
            "O001", "P001", "Q001"
        )

        private fun getRandomValueFromList(list: List<String>, random: Random): String {
            val size = list.size
            val index = random.nextInt(size)

            return list[index]
        }

        // random한 피자 메시지를 생성하고, 피자가게 명을 key로 나머지 정보를 value로 하여 Hashmap을 생성하여 반환.
        fun produceMsg(faker: Faker, random: Random, id: Int): HashMap<String, String> {
            val shopId = getRandomValueFromList(pizzaShop, random)
            val pizzaName = getRandomValueFromList(pizzaNames, random)

            val ordId = "ord$id"
            val customerName = faker.name().fullName()
            val phoneNumber = faker.phoneNumber().phoneNumber()
            val address = faker.address().streetAddress()
            val now = LocalDateTime.now()
            val message = """
                order_id:$ordId, shop:$shopId, pizza_name:$pizzaName, customer_name:$customerName, phone_number:$phoneNumber, address:$address, time:${
                now.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)
                )
            }
                """.trimIndent()

            val messageMap = HashMap<String, String>()
            messageMap["key"] = shopId
            messageMap["message"] = message

            return messageMap
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val seed: Long = 2024
            val random = Random(seed)

            val faker = Faker(random)

            for (i in 0..<60) {
                val message = produceMsg(faker, random, i)
                with(message) {
                    println("key: ${this["key"]}/message: ${this["message"]}")
                }
            }
        }
    }
}
