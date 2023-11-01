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
import NetGraphAlgebraDefs.{NetGraph, NodeObject}
import org.apache.spark.sql.SparkSession
import java.io.PrintWriter
import scala.collection.parallel.CollectionConverters._
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

// Even though professor has his own Random Walk function in NetGameAlgebraDefs which I noticed very late.
// I created my own as it was giving me trouble accessing variables outside of the random walk function.

object RandomWalkSparkApp {

  def main(args: Array[String]): Unit = {

    // Initialize Spark and adding master URL
    logger.info("Creating a Spark Session Object in Apache Spark")
    val spark: SparkSession = SparkSession.builder()
      .appName("RandomWalkSpark")
      .master("local")
      .getOrCreate()


    // Load the perturbed graph and collect its data such as nodes
    logger.info("Loading in the perturbed graph")
    val perturbedGraph: Option[NetGraph] = NetGraph.load("NetGameSimNetGraph_26-10-23-23-39-25.ngs.perturbed", "/Users/muzza/Desktop/projectTwo/TO_USE/")
    val netPerturbedGraph: NetGraph = perturbedGraph.get

    // Number of random walks to perform
    val numRandomWalks = 300

    // Maximum number of steps for each random walk
    val maxSteps = 1000

    // Start multiple random walks concurrently
    logger.info("Starting Random Walk")
    val walkResults = (1 to numRandomWalks).toList.par.map { _ =>
      val randomNode = getRandomNode(netPerturbedGraph)
      randomWalk(netPerturbedGraph, randomNode, maxSteps)
    }.toList
    logger.info("Random Walk Completed")


    // Save the results to output files
    logger.info("Saving Results")
    val outputPath = "/Users/muzza/Desktop/professorFiles/output" // Specify the output directory

    logger.info("Converting walkResults into a parallelized RDD and formatting the results as Strings")
    val rdd = spark.sparkContext.parallelize(walkResults.map(_.map(_.toString.stripPrefix("NodeObject(").stripSuffix(")")).mkString("\n")))

    logger.info("Saving the RDD as text file")
    rdd.saveAsTextFile(outputPath)

    logger.info("Stopping Spark to Release Resources")
    spark.stop()
    logger.info("Successful Stop")
  }


  // This function is created to grab a random node to begin the random walk
  // It takes in
  def getRandomNode(graph: NetGraph): NodeObject = {
    val nodesList = graph.sm.nodes().asScala.toList
    val randomIndex = scala.util.Random.nextInt(nodesList.length)
    nodesList(randomIndex)
  }

  // This function performs a random walk on the perturbed graph
  // It takes Perturbed Graph, starting node and
  def randomWalk(graph: NetGraph, startNode: NodeObject, maxSteps: Int): List[NodeObject] = {

    logger.info("Beginning the Random Walk starting from randomly chosen node")

    // Initializing the current node, step count, and path list
    var currentNode: NodeObject = startNode
    var steps: Int = 0
    var path: List[NodeObject] = List(currentNode)

    // Continuing the walk until the maximum number of steps is reached
    while (steps < maxSteps) {
      // Get a random connected node from the graph
      graph.getRandomConnectedNode(currentNode) match {
        case Some((nextNode, _)) =>
          // If a connected node is found, we update the current node, path, and step count
          currentNode = nextNode
          path = path :+ currentNode
          steps += 1
        case None =>
          // If no connected node is found, then we stop the walk by setting steps to maxSteps
          steps = maxSteps
      }
    }

    // Returning the path taken during the random walk
    path
  }

}