import org.apache.kafka.clients.producer.{Callback, RecordMetadata}

class DemoProducerCallback extends Callback {

  def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {
    if (e != null) println(e.printStackTrace())
  }
}
