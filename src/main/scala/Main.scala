import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

object Main extends App {

  private val getNextId: () => Int = {
    var i = 0
    () =>
      val next = i = i + 1
      i

  }

  def nextId = getNextId()

  val testProducer1: StringProducer = new StringProducer("test-topic-1")
  val consumer: StringConsumer = new StringConsumer(s"consumer-$nextId", 10)
  consumer.subscribe("test-topic-1")
  consumer.start

  def runLoop(message: String, exit: Boolean = false): Unit =
    (message, exit) match {
      case (_, true) => println("Bye bye")
      case ("generate", _) =>
        StringMessageGenerator.run(testProducer1).map { _ =>
          val (newMessage, exit) = getNewInput()
          runLoop(newMessage, exit)
        }
      case ("newConsumer", _) =>
        val consumer: StringConsumer =
          new StringConsumer(s"consumer-$nextId", 10)
        consumer.subscribe("test-topic-1")
        consumer.start
        val (newMessage, exit) = getNewInput()
        runLoop(newMessage, exit)
      case (msg, _) =>
        testProducer1.sendMessageAsync("key", msg)
        val (newMessage, exit) = getNewInput()
        runLoop(newMessage, exit)

    }

  def getNewInput(): (String, Boolean) = {
    val newMessage = scala.io.StdIn.readLine()
    println()
    val exit = newMessage == "exit"
    (newMessage, exit)
  }

  runLoop("")

//  val testAvroProducer1 = new AvroProducer("test-customer-topic-1")
//  testAvroProducer1.sendMessageAsync("1", Customer(1, "Julian Fenner"))

  Thread.sleep(60000)

}

object StringMessageGenerator {

  def getInt(range: Int) = scala.util.Random.nextInt(range)

  def getString() = Random.alphanumeric take 8 mkString ""

  def run(sp: StringProducer) = {
    val runTimes = getInt(10)
    Future.sequence(
      for (_ <- 1 to runTimes) yield {
        val sleep = getInt(500)
        Thread.sleep(sleep)
        val key = getInt(10000000).toString
        val message = getString()
        println(s"StringMessageGenerator: sending $key $message \n")
        sp.sendMessageAsync(key, message)
      }.map(_ => ())
    )

  }

}
