import NetGraphAlgebraDefs.NetGraph.logger
import org.json4s.JsonDSL.int2jvalue

import java.text.DecimalFormat
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.{SparkConf, SparkContext}


object SparkAttackerAuthenticate {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setAppName("FileProcessing")
      .setMaster("local[*]") // Set the master URL for local mode with all available cores
    val sc = new SparkContext(conf)

    // Load the input file as an RDD
    logger.info("loading the file to calculate stats")
    val inputRDD = sc.textFile("/Users/muzza/Desktop/professorFiles/simRankScore/above_0.9/part-00001")
    logger.info("File was successfully loaded")

    // This function is created to calculate statistics
    // This function is called by spark as its running files in parallel
    // It takes a CSV Line from a CSV File to break it down and search for valuable data for original & perturbed
    def checkAuthenticity(csvLine: String): String = {

      logger.info("Generating statistics")

      // Split the CSV line into fields (assuming a comma separator)
      val fields = csvLine.split(",")

      val perturbedValuableData: Boolean = fields(9).trim.toBoolean
      val originalValuableData: Boolean = fields(19).trim.toBoolean

      // Variables Declared for stats to use
      var ctl = scala.util.Random.nextInt(3) + 1
      var wtl = scala.util.Random.nextInt(3) + 1
      var successfulAttack: Boolean = false
      var dtl = scala.util.Random.nextInt(3) + 1
      var atl = scala.util.Random.nextInt(3) + 1
      var honeyPot: Boolean = false


      logger.info("Comparing Valuable Data in Original and Perturbed")
      (perturbedValuableData, originalValuableData) match {
        case (true, true) =>
          logger.info("Attack on the Target was successful")
          ctl += 1
          wtl -= 1
          dtl -= 1
          successfulAttack = true
          honeyPot = false
        case (false, true) =>
          logger.info("Attack on the Target was a Honeypot")
          ctl -= 1
          wtl -= 1
          dtl -= 1
          successfulAttack = false
          honeyPot = true
        case _ => dtl = 1
          logger.info("Attack Failed")
          ctl -= 1
          wtl -= 1
          dtl -= 1
          successfulAttack = false
          honeyPot = false
      }

      // Calculating btl, gtl, and rtl for the algorithm
      val btl = ctl + wtl // traceability links that may be incorrect (ctl + wtl)
      val gtl = dtl + atl // traceability links that are correct (dtl + atl)
      val rtl = btl + gtl // total number of traceability links (btl + rtl)

      // Node Id to represnt the Node that was Attacked in the original
      var info = s"Attacking Algorithm Statistics for Perturbed Node ${fields(10)}:"

      logger.info("Generating Statistics")
      val outputMessage = s"\n$info \nSuccessful Attack: $successfulAttack \nHoneyPot: $honeyPot \nBTL: $btl \nGTL: $gtl \nRTL: $rtl \nCTL: $ctl \nWTL: $wtl \nDTL: $dtl \nATL: $atl\n\n"
      logger.info("Statistics Successfully Generated")

      // Returning the final message to be outputted to the result of Spark
      outputMessage

    }

    logger.info("Applying Stats to each line in the input RDD")
    val resultRDD = inputRDD.map(checkAuthenticity)

    // Output Directory for Spark's output
    val outputDir = "/Users/muzza/Desktop/professorFiles/statistics"

    // Saving Spark's Results in the Output Directory
    resultRDD.saveAsTextFile(outputDir)

    logger.info("Stopping Spark")
    sc.stop()
    logger.info("Spark Stopped Successfully")
  }
}

