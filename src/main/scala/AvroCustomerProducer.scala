import java.util.Properties

import org.apache.kafka.clients.producer.{
  KafkaProducer,
  ProducerRecord,
  RecordMetadata
}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

class AvroProducer(topic: String) {

  val kafkaProperties = new Properties()

  kafkaProperties.put("bootstrap.servers", "localhost:9092")

  kafkaProperties.put(
    "key.serializer",
    "io.confluent.kafka.serializers.KafkaAvroSerializer"
  )
  kafkaProperties.put(
    "value.serializer",
    "io.confluent.kafka.serializers.KafkaAvroSerializer"
  )

  kafkaProperties.put("schema.registry.url", "schemaUrl")

  def sendMessageSync(key: String, value: Customer): Try[RecordMetadata] =
    Try(producer.send(createProducerRecord(key, value)).get)

  def sendMessageAsync(key: String, value: Customer): Future[RecordMetadata] =
    Future(
      producer
        .send(createProducerRecord(key, value), new DemoProducerCallback)
        .get
    )

  private def createProducerRecord(key: String, value: Customer) =
    new ProducerRecord(topic, key, value)

  private val producer: KafkaProducer[String, Customer] =
    new KafkaProducer[String, Customer](kafkaProperties)

}

case class Customer(customerId: Int, customerName: String)
