import com.typesafe.config.ConfigFactory

import java.net.{InetAddress, NetworkInterface, Socket}
import org.apache.spark._
import org.apache.spark.graphx._
import org.apache.spark.graphx.util.GraphGenerators
// To make some of the examples work we will also need RDD
import org.apache.spark.rdd.RDD


object Main {

  abstract class Graph[VD, ED] {
    val vertices: VertexRDD[VD]
    val edges: EdgeRDD[ED]
  }

  class VertexProperty()
  case class UserProperty(val name: String) extends VertexProperty
  case class ProductProperty(val name: String, val price: Double) extends VertexProperty

  // The graph might then have the type:
  var graph: Graph[VertexProperty, String] = null


  def main(args: Array[String]): Unit = {
    println("Hello world!")

    val config = ConfigFactory.load()
    val logger = org.slf4j.LoggerFactory.getLogger("Main")
    val hostName = InetAddress.getLocalHost.getHostName
    val ipAddr = InetAddress.getLocalHost.getHostAddress
    logger.info(s"Hostname: $hostName")
    logger.info(s"IP Address: $ipAddr")

    import org.apache.spark.sql.SparkSession
    //object SparkSessionTest extends App {

    // Create SparkSession object
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExamples.com")
      .getOrCreate();

    // Access spark context
    println("Spark App Name : " + spark.sparkContext.appName)

    val sc = spark.sparkContext
    // Create an RDD for the vertices
    val users: RDD[(VertexId, (String, String))] =
      sc.parallelize(Seq((3L, ("rxin", "student")), (7L, ("jgonzal", "postdoc")),
        (5L, ("franklin", "prof")), (2L, ("istoica", "prof"))))
    users.map(x => println(x))

    // Create an RDD for edges
    val relationships: RDD[Edge[String]] =
      sc.parallelize(Seq(Edge(3L, 7L, "collab"), Edge(5L, 3L, "advisor"),
        Edge(2L, 5L, "colleague"), Edge(5L, 7L, "pi")))
    // Define a default user in case there are relationship with missing user
    val defaultUser = ("John Doe", "Missing")
    // Build the initial Graph
    val graph = Graph(users, relationships, defaultUser)
    println(graph)

    // }

  }
}