import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.serialization.Serdes

import java.util.Properties
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.serialization.Serdes._


object WordToUpper {

  val INPUT_TOPIC = "Stream-input"
  val OUTPUT_TOPIC = "Stream-output"

  def main(args: Array[String]) = {
    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "new_kafka_streams_demo")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ":9092")
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.getClass)

    val topology = getTopology
    val stream = new KafkaStreams(topology, props)
    stream.start()
  }

  def getTopology: Topology = {
    val builder = new StreamsBuilder()

    val word = builder.stream[String, String](INPUT_TOPIC)

    word.mapValues(word => word.toUpperCase()).to(OUTPUT_TOPIC)

    val numbers = builder.stream[String, String](INPUT_TOPIC)
    val positive = numbers.filter((_, value) => value.toInt >= 0)
    val negative = numbers.filter((_, value) => value.toInt < 0)

    positive.to(OUTPUT_TOPIC)
    negative.to(OUTPUT_TOPIC)

    builder.build()
  }
}
