import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

//Example using the kafka client directly
// See chapter 3 of Kafka book
object Main extends App {

  // 1 Setup kafka producer (page 44)
  val kafkaProps = new Properties()
  kafkaProps.put("bootstrap.servers", "localhost:9092")

  kafkaProps.put(
    "key.serializer",
    "org.apache.kafka.common.serialization.StringSerializer"
  )

  kafkaProps.put(
    "value.serializer",
    "org.apache.kafka.common.serialization.StringSerializer"
  )

  val producer: KafkaProducer[String, String] =
    new KafkaProducer[String, String](kafkaProps)

  // 2 Send a message (page 46)

  val record: ProducerRecord[String, String] =
    new ProducerRecord("CustomerCountry", "Precision Products", "France")
  producer.send(record)
  //  catch {
  //    case e: Exception =>
  //      e.printStackTrace()
  //  }
}
