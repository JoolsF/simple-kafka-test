import java.util.Properties

import org.apache.kafka.clients.producer.{
  KafkaProducer,
  ProducerRecord,
  RecordMetadata
}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

class StringProducer(topic: String) {

  val kafkaProperties = new Properties()

  kafkaProperties.put("bootstrap.servers", "localhost:9092")

  kafkaProperties.put(
    "key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer"
  )

  kafkaProperties.put(
    "value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer"
  )

  def sendMessageSync(key: String, value: String): Try[RecordMetadata] =
    Try(producer.send(createProducerRecord(key, value)).get)

  def sendMessageAsync(key: String, value: String): Future[RecordMetadata] = {
    println(s"StringProducer: producing $key $value to $topic")
    Future(
      producer
        .send(createProducerRecord(key, value), new DemoProducerCallback)
        .get
    )
  }

  private def createProducerRecord(key: String, value: String) =
    new ProducerRecord(topic, key, value)

  private val producer: KafkaProducer[String, String] =
    new KafkaProducer[String, String](kafkaProperties)

}
