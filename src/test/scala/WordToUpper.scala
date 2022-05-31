import org.apache.kafka.streams.{KeyValue, TestInputTopic, TestOutputTopic, Topology, TopologyTestDriver}
import org.apache.kafka.streams.scala.serialization.Serdes.stringSerde
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class WordToUpper extends AnyFlatSpec with should.Matchers {

  it should "return topology" in {
    val topology: Topology = WordToUpper.getTopology
    val testDriver = new TopologyTestDriver(topology)
    val inputTopic: TestInputTopic[String, String] = testDriver.createInputTopic(WordToUpper.INPUT_TOPIC, stringSerde.serializer, stringSerde.serializer())
    val outputTopic: TestOutputTopic[String, String] = testDriver.createOutputTopic(WordToUpper.OUTPUT_TOPIC, stringSerde.deserializer, stringSerde.deserializer)

    inputTopic.pipeInput("something")
    outputTopic.readKeyValue() shouldBe KeyValue.pair("Something")
  }

}
