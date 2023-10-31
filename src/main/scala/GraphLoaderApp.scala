import NetGraphAlgebraDefs.{Action, NetGraph, NodeObject}
import com.google.common.graph.EndpointPair
import org.json4s.JSet
import org.slf4j.LoggerFactory
import org.slf4j.LoggerFactory

import java.util.{Set => JSet}
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.jdk.CollectionConverters.CollectionHasAsScala

// Define a class to represent NodeObject (you may need to adjust this based on your actual implementation)
//case class NodeObject(id: Int, properties: Map[String, Any])

// Define the NetGraph class (adjust this based on your actual implementation)
//case class NetGraph(sm: SomeGraphManager) {
//  // Add methods and properties as needed
//}

object GraphLoaderApp {
  // Initialize the logger
  val logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    try {
      logger.info("Loading in original graph ngs and perturbed graph ngs files using NetGraph.load function:")
      val originalGraph: Option[NetGraph] = NetGraph.load("NetGameSimNetGraph_26-10-23-23-39-25.ngs", "/Users/muzza/Desktop/projectTwo/TO_USE/")
      val perturbedGraph: Option[NetGraph] = NetGraph.load("NetGameSimNetGraph_26-10-23-23-39-25.ngs.perturbed", "/Users/muzza/Desktop/projectTwo/TO_USE/")


      logger.info("Gathering information of the graphs")
      val netOriginalGraph: NetGraph = originalGraph.get // getiting original graph info
      val netPerturbedGraph: NetGraph = perturbedGraph.get // getiting perturbed graph info

      logger.info("Storing nodes for in a list for original graph")
      val originalGraphNodes: java.util.Set[NodeObject] = netOriginalGraph.sm.nodes()

      logger.info("Storing nodes for in a list for perturbed graph")
      val perturbedGraphNodes: java.util.Set[NodeObject] = netPerturbedGraph.sm.nodes()


      val originalGraphEdges: java.util.Set[EndpointPair[NodeObject]] = netOriginalGraph.sm.edges()
      val originalEdgeList = originalGraphEdges.asScala.toList
      val perturbedGraphEdges: java.util.Set[EndpointPair[NodeObject]] = netPerturbedGraph.sm.edges()
      val perturbedEdgeList = perturbedGraphEdges.asScala.toList


//      originalGraphNodes.foreach { node =>
//        if(node.valuableData){
//          println(s"Node Id: ${node.id}")
//        }
//      }

      def randomwalk(netGraph: NetGraph): Unit = {


        perturbedGraphNodes.foreach { node =>

            if(node.id == 9){
              println(s"Node Id: ${node.id}")
              println()
              println(s"Random: ${netGraph.getRandomConnectedNode(node)}")
            }

        }


      }
      randomwalk(netPerturbedGraph)



      println()


      perturbedEdgeList.foreach { edge =>
        if(edge.source.id == 9) {
          println(s"edge: ${edge}")
        }
      }





      // Perform further processing with originalGraphNodes and perturbedGraphNodes
    } catch {
      case e: Exception =>
        logger.error("An error occurred:", e)
    }
  }
}
