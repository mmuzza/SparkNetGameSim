import NetGraphAlgebraDefs.NetGraph.logger
import NetGraphAlgebraDefs.{Action, NetGraph, NodeObject}
import org.apache.spark.sql.SparkSession
import org.apache.spark.graphx._
import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext

import scala.jdk.CollectionConverters.CollectionHasAsScala

import org.apache.spark.sql.SparkSession
import java.io.PrintWriter

import org.apache.spark.sql.SparkSession
import java.io.PrintWriter

/*
object RandomWalkApp {
  def main(args: Array[String]): Unit = {
    // Initialize Spark
    val spark: SparkSession = SparkSession.builder()
      .appName("RandomWalk")
      .master("local")
      .getOrCreate()

    // Load your perturbed graph
    val perturbedGraph: Option[NetGraph] = NetGraph.load("NetGameSimNetGraph_26-10-23-23-39-25.ngs.perturbed", "/Users/muzza/Desktop/projectTwo/TO_USE/")
    val netPerturbedGraph: NetGraph = perturbedGraph.get

    // Maximum number of steps for the random walk
    val maxSteps = 10000

    // Perform the random walk
    val walkResult = randomWalk(netPerturbedGraph, maxSteps)

    // Store results in a file
    val outputPath = "/Users/muzza/Desktop/professorFiles/output/file.txt"
    val writer = new PrintWriter(outputPath)
    walkResult.foreach(node => writer.println(node))
    writer.close()

    // Stop Spark
    spark.stop()
  }

  def randomWalk(graph: NetGraph, maxSteps: Int): List[NodeObject] = {
    var currentNode: NodeObject = graph.initState
    var steps: Int = 0
    var path: List[NodeObject] = List(currentNode)
    var visited: Set[NodeObject] = Set(currentNode)

    while (steps < maxSteps && visited.size < graph.totalNodes) {
      graph.getRandomConnectedNode(currentNode) match {
        case Some((nextNode, _)) if !visited.contains(nextNode) =>
          currentNode = nextNode
          path = path :+ currentNode
          visited += currentNode
          steps += 1
        case _ =>
          steps = maxSteps
      }
    }

    path
  }
}
*/

import NetGraphAlgebraDefs.{NetGraph, NodeObject}
import org.apache.spark.sql.SparkSession
import java.io.PrintWriter
import scala.collection.parallel.CollectionConverters._

/*
object RandomWalkSparkApp {

  def main(args: Array[String]): Unit = {
    // Initialize Spark
    val spark: SparkSession = SparkSession.builder()
      .appName("RandomWalkSpark")
      .master("local")
      .getOrCreate()

    // Load your perturbed graph
    val perturbedGraph: Option[NetGraph] = NetGraph.load("NetGameSimNetGraph_26-10-23-23-39-25.ngs.perturbed", "/Users/muzza/Desktop/projectTwo/TO_USE/")
    val netPerturbedGraph: NetGraph = perturbedGraph.get

    // Number of random walks to perform
    val numRandomWalks = 300

    // Maximum number of steps for each random walk
    val maxSteps = 1000

    // Start multiple random walks concurrently
    val walkResults = (1 to numRandomWalks).toList.par.map { _ =>
      val randomNode = getRandomNode(netPerturbedGraph)
      randomWalk(netPerturbedGraph, randomNode, maxSteps)
    }.toList

    // Combine results and store them in the same output file

//    val outputPath = "/Users/muzza/Desktop/professorFiles/output/file.txt"
//    val writer = new PrintWriter(outputPath)
//    walkResults.flatten.foreach(node => writer.println(node))
//    walkResults.flatten.foreach(node => println(s"Node: $node"))
//    println()
//    writer.close()

    val outputPath = "/Users/muzza/Desktop/professorFiles/output/file.txt"
    val writer = new PrintWriter(outputPath)

    // Add the header
    val header = "id, children, props, currentDepth, propValueRange, MaxDepth, MaxBranchingFactor, MaxProperties, storedValue, valuableData"
    writer.println(header)

    walkResults.flatten.foreach { node =>
      val nodeStr = node.toString
      val content = nodeStr.substring(nodeStr.indexOf("(") + 1, nodeStr.lastIndexOf(")"))
      writer.println(content)
    }
    writer.close()


    // Stop Spark
    spark.stop()
  }

  def getRandomNode(graph: NetGraph): NodeObject = {
    val nodesList = graph.sm.nodes().asScala.toList
    val randomIndex = scala.util.Random.nextInt(nodesList.length)
    nodesList(randomIndex)
  }

  def randomWalk(graph: NetGraph, startNode: NodeObject, maxSteps: Int): List[NodeObject] = {
    var currentNode: NodeObject = startNode
    var steps: Int = 0
    var path: List[NodeObject] = List(currentNode)

    while (steps < maxSteps) {
      graph.getRandomConnectedNode(currentNode) match {
        case Some((nextNode, _)) =>
          currentNode = nextNode
          path = path :+ currentNode
          steps += 1
        case None =>
          steps = maxSteps
      }
    }

    path
  }
}
*/

import org.apache.spark.sql.SparkSession



/* ::Explanation for the Random Walk being performed Using Spark:: */

/* In the following Code we are performing a Random Walk on Perturbed Nodes */

/* 1. Here we create a SparkSession at the beginning */

/* 2. Multiple random walks are initiated concurrently using the .par method on the (1 to numRandomWalks).toList range. */

/*  - This parallelizes the creation of random walks and allows them to execute concurrently taking advantage of multiple CPU cores. */

/*  - The results of these random walks are stored in the walkResults list, where each result is a list of NodeObject. */

/* 3. After the parallel computation of random walks the results are transformed into an RDD (rdd) using parallelize. */

/*  - This RDD will then be saved as text files. */

/* 4. The results are saved to the specified output directory in text files. */


object RandomWalkSparkApp {

  def main(args: Array[String]): Unit = {
    // Initialize Spark
    val spark: SparkSession = SparkSession.builder()
      .appName("RandomWalkSpark")
      .master("local")
      .getOrCreate()

    // Load your perturbed graph
    val perturbedGraph: Option[NetGraph] = NetGraph.load("NetGameSimNetGraph_26-10-23-23-39-25.ngs.perturbed", "/Users/muzza/Desktop/projectTwo/TO_USE/")
    val netPerturbedGraph: NetGraph = perturbedGraph.get

    // Number of random walks to perform
    val numRandomWalks = 300

    // Maximum number of steps for each random walk
    val maxSteps = 1000

    // Start multiple random walks concurrently
    val walkResults = (1 to numRandomWalks).toList.par.map { _ =>
      val randomNode = getRandomNode(netPerturbedGraph)
      randomWalk(netPerturbedGraph, randomNode, maxSteps)
    }.toList


    // Save the results to output files
    val outputPath = "/Users/muzza/Desktop/professorFiles/output" // Specify the output directory
    val rdd = spark.sparkContext.parallelize(walkResults.map(_.map(_.toString.stripPrefix("NodeObject(").stripSuffix(")")).mkString("\n")))
    rdd.saveAsTextFile(outputPath)

    // Stop Spark
    spark.stop()
  }

  def getRandomNode(graph: NetGraph): NodeObject = {
    val nodesList = graph.sm.nodes().asScala.toList
    val randomIndex = scala.util.Random.nextInt(nodesList.length)
    nodesList(randomIndex)
  }

  def randomWalk(graph: NetGraph, startNode: NodeObject, maxSteps: Int): List[NodeObject] = {
    var currentNode: NodeObject = startNode
    var steps: Int = 0
    var path: List[NodeObject] = List(currentNode)

    while (steps < maxSteps) {
      graph.getRandomConnectedNode(currentNode) match {
        case Some((nextNode, _)) =>
          currentNode = nextNode
          path = path :+ currentNode
          steps += 1
        case None =>
          steps = maxSteps
      }
    }

    path
  }
}




