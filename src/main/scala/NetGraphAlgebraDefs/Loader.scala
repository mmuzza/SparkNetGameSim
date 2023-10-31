package NetGraphAlgebraDefs

import NetGraphAlgebraDefs.{NetGraph, NodeObject}
import Utilz.NGSConstants.{CONFIGENTRYNAME, obtainConfigModule}

import java.net.{InetAddress, NetworkInterface, Socket}
import org.apache.spark._
import org.apache.spark.graphx._
import org.apache.spark.graphx.util.GraphGenerators
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.slf4j.Logger
import com.typesafe.config.ConfigFactory
import com.typesafe.config.{Config, ConfigFactory}
import com.google.common.graph.{EndpointPair, MutableValueGraph, ValueGraph}
import NetGraphAlgebraDefs.{Action, GraphPerturbationAlgebra, NetGraph, NetModelAlgebra, NodeObject}
import NetModelAnalyzer.Analyzer
import Randomizer.SupplierOfRandomness
import Utilz.{CreateLogger, NGSConstants}
import com.google.common.graph.{EndpointPair, ValueGraph}

import java.util.concurrent.{ThreadLocalRandom, TimeUnit}
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import com.typesafe.config.ConfigFactory
import guru.nidi.graphviz.engine.Format
import org.slf4j.Logger

import java.net.{InetAddress, NetworkInterface, Socket}
import scala.collection.parallel.ParSeq
import scala.util.{Failure, Random, Success}
import NetGraphAlgebraDefs.NetGraph._

import scala.collection.JavaConverters._
import java.io.{BufferedWriter, File, FileWriter, PrintWriter}
import java.nio.file.{Files, Paths, StandardCopyOption}
import scala.collection.mutable.ListBuffer
import scala.io.Source
import java.io.{BufferedWriter, FileWriter}
import NetGraphAlgebraDefs.GraphPerturbationAlgebra.ModificationRecord
import NetGraphAlgebraDefs.NetModelAlgebra.{actionType, outputDirectory}
import NetGraphAlgebraDefs.{Action, GraphPerturbationAlgebra, NetGraph, NetModelAlgebra, NodeObject}
import NetModelAnalyzer.Analyzer
import Randomizer.SupplierOfRandomness
import Utilz.{CreateLogger, NGSConstants}
import com.google.common.graph.{EndpointPair, ValueGraph}

import java.util.concurrent.{ThreadLocalRandom, TimeUnit}
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import com.typesafe.config.ConfigFactory
import guru.nidi.graphviz.engine.Format
import org.slf4j.Logger

import java.net.{InetAddress, NetworkInterface, Socket}
import scala.collection.parallel.ParSeq
import scala.util.{Failure, Random, Success}
import NetGraphAlgebraDefs.NetGraph._

import scala.collection.JavaConverters._
import java.io.{BufferedWriter, File, FileWriter, PrintWriter}
import java.nio.file.{Files, Paths, StandardCopyOption}
import scala.collection.mutable.ListBuffer
import scala.io.Source
import java.io.{BufferedWriter, FileWriter}
import org.apache.spark.sql.SparkSession
object Loader {

  abstract class Graph[VD, ED] {
    val vertices: VertexRDD[VD]
    val edges: EdgeRDD[ED]
  }

  class VertexProperty()

  case class UserProperty(val name: String) extends VertexProperty
  case class ProductProperty(val name: String, val price: Double) extends VertexProperty

  // The graph might then have the type:
  var graph: Graph[VertexProperty, String] = null

  // Assume the SparkContext has already been constructed
  val sc: SparkContext = null

  // Create an RDD for the vertices
  val users: RDD[(VertexId, (String, String))] =
    sc.parallelize(Seq((3L, ("rxin", "student")), (7L, ("jgonzal", "postdoc")),
      (5L, ("franklin", "prof")), (2L, ("istoica", "prof"))))

  // Create an RDD for edges
  val relationships: RDD[Edge[String]] =
    sc.parallelize(Seq(Edge(3L, 7L, "collab"), Edge(5L, 3L, "advisor"),
      Edge(2L, 5L, "colleague"), Edge(5L, 7L, "pi")))

  // Define a default user in case there are relationship with missing user
  val defaultUser = ("John Doe", "Missing")

  type NetStateMachine = MutableValueGraph[NodeObject, Action]


  // Assuming you have a list of vertices and a list of edges in your NetGraph => RDDs
  //val vertices: RDD[(VertexId, String)] = sc.parallelize(originalGraph.vertices.map(vertex => (vertex.id, vertex.value)))
  //val edges: RDD[Edge[String]] = sc.parallelize(originalGraph.edges.map(edge => Edge(edge.srcId, edge.dstId, edge.value)))

  // Create a GraphX graph from vertices and edges
  //val graph = Graph(vertices, edges)
  val graph1 = Graph(users, relationships, defaultUser)


}
