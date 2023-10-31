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
//    val inputRDD = sc.textFile("/Users/muzza/Desktop/professorFiles/simRankScore/equal_to_0.9/part-00000")
//    val inputRDD = sc.textFile("/Users/muzza/Desktop/professorFiles/simRankScore/below_0.9/part-00001")
    val inputRDD = sc.textFile("/Users/muzza/Desktop/professorFiles/simRankScore/above_0.9/part-00001")

    // Define the checkAuthenticity function
    def checkAuthenticity(csvLine: String): String = {

      // Split the CSV line into fields (assuming a comma separator)
      val fields = csvLine.split(",")

      val perturbedValuableData: Boolean = fields(9).trim.toBoolean
      val originalValuableData: Boolean = fields(19).trim.toBoolean

      val smoothingFactor = 0.3
      var ctl = (1.0 - smoothingFactor) * scala.util.Random.nextDouble() + smoothingFactor * 0.5
      var wtl = (1.0 - smoothingFactor) * scala.util.Random.nextDouble() + smoothingFactor * 0.4
      var dtl = (1.0 - smoothingFactor) * scala.util.Random.nextDouble() + smoothingFactor * 0.3
      var atl = (1.0 - smoothingFactor) * scala.util.Random.nextDouble() + smoothingFactor * 0.2
      var successfulAttack: Boolean = false
      var honeyPot: Boolean = false


      (perturbedValuableData, originalValuableData) match {
        case (true, true) =>
          ctl += 0.3
          wtl -= 0.3
          dtl -= 0.4
          successfulAttack = true
          honeyPot = false
        case (false, true) =>
          ctl -= 0.3
          wtl -= 0.3
          dtl -= 0.4
          successfulAttack = false
          honeyPot = true
        case _ => dtl = 1
          ctl -= 0
          wtl -= 0.3
          dtl -= 0.4
          successfulAttack = false
          honeyPot = false
      }

      val btl = ctl + wtl // traceability links that may be incorrect (ctl + wtl)
      val gtl = dtl + atl // traceability links that are correct (dtl + atl)
      val rtl = btl + gtl // total number of traceability links (btl + rtl)

      var info = s"Attacking Algorithm Statistics for Perturbed Node ${fields(0)}:"

      logger.info("Calculating Statistics")
      val outputMessage = s"\n$info \nSuccessful Attack: $successfulAttack \nHoneyPot: $honeyPot \nBTL: $btl \nGTL: $gtl \nRTL: $rtl \nCTL: $ctl \nWTL: $wtl \nDTL: $dtl \nATL: $atl\n\n"

      outputMessage

    }

    // Apply the checkAuthenticity function to each line in the input RDD
    val resultRDD = inputRDD.map(checkAuthenticity)

    // Specify the output directory
    val outputDir = "/Users/muzza/Desktop/professorFiles/statistics"

    // Save the results to the specified output directory
    resultRDD.saveAsTextFile(outputDir)

    sc.stop()
  }
}

