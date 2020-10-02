import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}

import scala.collection.convert.ImplicitConversions.`iterator asScala`
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class StringConsumer(consumerGroupId: String, pollIntervalMs: Int) {

  val props = new Properties
  props.put("bootstrap.servers", "localhost:9092")
  props.put("group.id", consumerGroupId)
  props.put(
    "key.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer"
  )
  props.put(
    "value.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer"
  )
  private val consumer = new KafkaConsumer[String, String](props)

  def subscribe(topic: String): Unit = {
    consumer.subscribe(Collections.singletonList(topic))
  }

  def start =
    Future {
      try {
        while (true) {
          consumer
            .poll(pollIntervalMs)
            .iterator()
            .toList
            .foreach((record: ConsumerRecord[String, String]) =>
              println(
                s"StringConsumer: $consumerGroupId consumed record: ${convertRecord(record)} \n"
              )
            )
        }
      } finally {
        consumer.close();
      }
    }

  def convertRecord(record: ConsumerRecord[String, String]) = {
    s"topic: ${record.topic()} key: ${record.key()} value: ${record.value()}"
  }

}
