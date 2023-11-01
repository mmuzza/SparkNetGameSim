import NetGraphAlgebraDefs.{NetGraph, NodeObject}
import NetGraphAlgebraDefs.NetGraph.logger

import java.io._
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.io.Source

object storingFiles {

  // This file store nodes and the properties to CSV for Original Graph
  def writeNodesToCSV(outputPath: String, nodes: java.util.Set[NodeObject]): Unit = {
    val writer = new PrintWriter(outputPath)

    // Write the CSV header with property names
    val header = "id, children, props, currentDepth, propValueRange, MaxDepth, MaxBranchingFactor, MaxProperties, storedValue, valuableData"
    writer.println(header)

    nodes.foreach { node =>
      val properties = node.toString.split("\\(")(1).split("\\)")(0)
      writer.println(properties)
    }

    writer.close()
  }

  //--------------------------------------------------------------------------------


  // Function to compare lines from perturbed file with lines from original file and write to CSV
  def combineAndWriteToCSV(perturbedFilePath: String, originalFilePath: String, combinedFilePath: String): Unit = {
    // Read the contents of the perturbed and original files
    val perturbedLines = Source.fromFile(perturbedFilePath).getLines().toList.drop(1) // Skip header
    val originalLines = Source.fromFile(originalFilePath).getLines().toList.drop(1) // Skip header

    // Create a CSV file to write the combined lines
    val csvFile = new File(combinedFilePath)
    val csvWriter = new PrintWriter(new FileWriter(csvFile))

    // Write the header to the CSV file
    val header = "P.Id, P.Children, P.Props, P.CurrentDepth, P.PropValueRange, P.MaxDepth, P.MaxBranchingFactor, P.MaxProperties, P.StoredValue, P.ValuableData, O.Id, O.Children, O.Props, O.CurrentDepth, O.PropValueRange, O.MaxDepth, O.MaxBranchingFactor, O.MaxProperties, O.StoredValue, O.ValuableData"
    csvWriter.println(header)

    // Compare and write lines
    perturbedLines.foreach { perturbedLine =>
      originalLines.foreach { originalLine =>
        // Combine the lines and write to CSV
        val combinedLine = s"$perturbedLine, $originalLine"
        csvWriter.println(combinedLine)
      }
    }

    // Close the CSV file writer
    csvWriter.close()
  }


  def main(args: Array[String]): Unit = {

    // Here we load in the Original Ngs Graph
    logger.info("Loading in Original Graph ngs file using NetGraph.load function:")
    val originalGraph: Option[NetGraph] = NetGraph.load("NetGameSimNetGraph_26-10-23-23-39-25.ngs", "/Users/muzza/Desktop/projectTwo/TO_USE/")
    logger.info("Original Graph was successfully loaded")

    // Getting information of Original Graph
    logger.info("Gathering Information of the Original Graph")
    val netOriginalGraph: NetGraph = originalGraph.get // getiting original graph info
    logger.info("Information successfully extracted for Original Graph")

    // Gathering the Nodes in original graph
    logger.info("Storing Nodes in a list for Original Graph")
    val originalGraphNodes: java.util.Set[NodeObject] = netOriginalGraph.sm.nodes()
    logger.info("Original Node were successfully stored within a list")



    // Creating Csv file to store all of the original nodes in a csv file
    // This will be used in creating Cartesian Product of original X perturbed
    logger.info("Storing Original Graph Nodes in a CSV file")
    val originalFilePath = "/Users/muzza/Desktop/professorFiles/originalCsv/file.txt"
    writeNodesToCSV(originalFilePath, originalGraphNodes)
    logger.info("Original Graph Nodes were successfully stored in a Csv File")



    // Creating a Cartesian product of Perturbed X Original
    // Perturbed Nodes are those generated via Random Walk using Spark
    // Original Nodes are were directly stored in .txt
    logger.info("Creating Csv File to store each Perturbed Node X Original Nodes in a CSV File")
    val perturbedFilePath = "/Users/muzza/Desktop/professorFiles/output/part-00000"
    val combinedFilePath = "/Users/muzza/Desktop/professorFiles/combinedCsv/combined.csv"
    combineAndWriteToCSV(perturbedFilePath, originalFilePath, combinedFilePath)
    logger.info("Combined Csv of Perturbed X Original Graphs was Successfully created")


  }
}
