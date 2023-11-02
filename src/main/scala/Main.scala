import NetGraphAlgebraDefs.NetGraph.logger
import Utilz.NGSConstants.{CONFIGENTRYNAME, obtainConfigModule}
import com.typesafe.config.{Config, ConfigFactory}

import java.net.{InetAddress, NetworkInterface, Socket}
import org.apache.spark._
import org.apache.spark.graphx._
import org.apache.spark.graphx.util.GraphGenerators
// To make some of the examples work we will also need RDD
import org.apache.spark.rdd.RDD


object Main {


  def main(args: Array[String]): Unit = {


    // Used by: storingFiles & RandomWalkSparkApp Classes:
    val Directory = "/Users/muzza/Desktop/projectTwo/TO_USE/"


    // used by: storingFiles Class:
    val originalGraphFileName = "NetGameSimNetGraph_26-10-23-23-39-25.ngs"
    val originalNodesCsvPath = "/Users/muzza/Desktop/professorFiles/originalCsv/file.txt"
    val perturbedFileFromRandomWalkPath = "/Users/muzza/Desktop/professorFiles/output/part-00000"
    val perturbedXoriginalPath = "/Users/muzza/Desktop/professorFiles/combinedCsv/combined.csv"


    // Used by RandomWalkSparkApp Class:
    val perturbedGraphFileName = "NetGameSimNetGraph_26-10-23-23-39-25.ngs.perturbed"
    val randomWalkOutputPath = "/Users/muzza/Desktop/professorFiles/output"

    // Used by SparkAttackerAuthenticity Class:
    val calculateStatsInputFile = "/Users/muzza/Desktop/professorFiles/simRankScore/above_0.9/part-00001"
    val statsOutput = "/Users/muzza/Desktop/professorFiles/statistics"



    // Task #1
    // This is a function call to Execute a random walk on Perturbed Graph using Spark (Parallelization)
    // It will create a txt file which will store all nodes from perturbed that were walked through
    RandomWalkSparkApp.executeRandomWalk(perturbedGraphFileName, Directory, randomWalkOutputPath)


    // Task #2
    // Storing the Original Graph Node in a txt file
    // Then Taking the two txt files of perturbed and original and forming a cartesian product
    // The cartesian product will be putting each Perturbed node from Random Walk against all Original Node in a CSV file
    storingFiles.callToExecute(originalGraphFileName, Directory, originalNodesCsvPath, perturbedFileFromRandomWalkPath, perturbedXoriginalPath)


    // Task #3
    // Taking the CSV that holds the cartesian product of original X perturbed from storingFiles class
    // And on each line of the CSV calling the Sim Rank using Spark (Running in parallel)
    // Spark will generate three csv files.
    // - First one of nodes that are equal to threshold 0.9 indicating nodes are matched
    // - Second one of nodes that are above the threshold 0.9 indicating nodes were modified
    // - Third one of nodes that are below the threshold 0.9 indicating nodes were removed.
    SparkSimRank.executeSimRankOnRandomWalk()


    // Task #4
    // Finally Using the files generate by SimRank algorithm with Spark
    // We will take each file and use spark to run them in parallel and call our designated function to generate
    // stats on the attack being successful or not for each node match
    SparkAttackerAuthenticate.executeCalculatingStats(calculateStatsInputFile, statsOutput)


    // End of program


  }

}