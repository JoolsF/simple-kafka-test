import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

object StringProducerGenerator {

  def getInt(range: Int) = scala.util.Random.nextInt(range)

  def getString() = Random.alphanumeric take 8 mkString ""

  def run(sp: StringProducer): Future[IndexedSeq[Unit]] = {
    val runTimes = getInt(10)
    Future.sequence(
      for (_ <- 1 to runTimes) yield {
        val sleep = getInt(500)
        Thread.sleep(sleep)
        val key = getInt(10000000).toString
        val message = getString()
        println(s"StringProducerGenerator: sending $key $message \n")
        sp.sendMessageAsync(key, message)
      }.map(_ => ())
    )

  }

}
