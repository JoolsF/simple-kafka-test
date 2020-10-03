object Main extends App {

  args.toList match {
    case "prod" :: topicName :: _ =>
      println(s"Creating new producer for topic: $topicName")
      new ProducerRunner(topicName)
    case "cons" :: consumerId :: topicName :: _ =>
      println(
        s"Creating new consumer with id $consumerId for topic: $topicName"
      )
      val consumer: StringConsumer =
        new StringConsumer(s"consumer-$consumerId", 100)
      consumer.subscribe(topicName)
      consumer.start
    case _ =>
      println("Invalid input. Bye bye.")
      System.exit(1)
  }

}

class ProducerRunner(topicName: String) {

  val testProducer1: StringProducer = new StringProducer(topicName)

  import InputHelper._

  def runLoop(message: String, exit: Boolean = false): Unit =
    (message, exit) match {
      case (_, true) => println("Bye bye")
      case ("generate", _) =>
        StringProducerGenerator.run(testProducer1)
        Thread.sleep(4000) //This is a temporary solution!
        val (newMessage, exit) = getNewInput()
        runLoop(newMessage, exit)
      case (msg, _) if (msg.nonEmpty) =>
        testProducer1.sendMessageAsync("key", msg)
        val (newMessage, exit) = getNewInput()
        runLoop(newMessage, exit)
      case _ =>
        val (newMessage, exit) = getNewInput()
        runLoop(newMessage, exit)
    }

  runLoop("")

}

object InputHelper {
  def getNewInput(): (String, Boolean) = {
    print("\n Next instruction: ")
    val newMessage = scala.io.StdIn.readLine()
    println()
    Thread.sleep(500)
    val exit = newMessage == "exit"
    (newMessage, exit)
  }

}
