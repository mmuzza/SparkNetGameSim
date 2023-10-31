import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import java.text.DecimalFormat


object SparkSimRank {

  def main(args: Array[String]): Unit = {
    // Create a SparkConf and SparkContext
    val conf = new SparkConf()
      .setAppName("SimRankApp")
      .setMaster("local") // Use "local" for local mode, or specify your Spark cluster URL here
    val sc = new SparkContext(conf)

    // Load data from a CSV file as an RDD
    val data = sc.textFile("/Users/muzza/Desktop/professorFiles/combinedCsv/combined.csv")

    // Sim Rank algorithm compares the Nodes properties of original with the perturbed
    // All properties are used excluding the Node "ID"
    // It then generates a score determining whether it was removed, modified, or added based on the threshold.
    // The threshold for match is set to 0.9, anything above is modified, and below is considered removed
    def calculateSimRank(csvLine: String): Double = {

      println(s"Line: $csvLine")
      println

      // Skip the header line
      if (csvLine.startsWith("P.Id")) {
        return 0.0
      }

      // Split the CSV line by comma to extract fields
      val fields = csvLine.split(",")

      var score: Double = 0.0


      // val perturbedId = fields(9).trim.toDouble
      val perturbedChildren = fields(1).trim.toDouble
      val perturbedProps = fields(2).trim.toDouble
      val perturbedCurrentDepth = fields(3).trim.toDouble
      val perturbedPropValueRange = fields(4).trim.toDouble
      val perturbedMaxDepth = fields(5).trim.toDouble
      val perturbedMaxBranchingFactor = fields(6).trim.toDouble
      val perturbedMaxProperties = fields(7).trim.toDouble
      val perturbedStoredValue = fields(8).trim.toDouble
      val perturbedValuableData: Boolean = fields(9).trim.toBoolean


      // val originalId = fields(0).trim.toDouble
      val originalChildren = fields(11).trim.toDouble
      val originalProps = fields(12).trim.toDouble
      val originalCurrentDepth = fields(13).trim.toDouble
      val originalPropValueRange = fields(14).trim.toDouble
      val originalMaxDepth = fields(15).trim.toDouble
      val originalMaxBranchingFactor = fields(16).trim.toDouble
      val originalMaxProperties = fields(17).trim.toDouble
      val originalStoredValue = fields(18).trim.toDouble
      val originalValuableData: Boolean = fields(9).trim.toBoolean


      if (originalChildren == perturbedChildren) {
        score += 0.1
      }
      else if (originalChildren < perturbedChildren) {
        score += 0.02
      }
      else {
        score += 0.02
      }


      if (originalProps == perturbedProps) {
        score += 0.1
      }
      else if (originalProps < perturbedProps) {
        score += 0.02
      }
      else {
        score += 0.02
      }


      if (originalCurrentDepth == perturbedCurrentDepth) {
        score += 0.1
      }
      else if (originalCurrentDepth < perturbedCurrentDepth) {
        score += 0.02
      }
      else {
        score += 0.02
      }


      if (originalPropValueRange == perturbedPropValueRange) {
        score += 0.2
      }
      else if (originalPropValueRange < perturbedPropValueRange) {
        score += 0.1
      }
      else {
        score += 0.2
      }


      if (originalMaxDepth == perturbedMaxDepth) {
        score += 0.1
      }
      else if (originalMaxDepth < perturbedMaxDepth) {
        score += 0.02
      }
      else {
        score += 0.02
      }

      if (originalMaxBranchingFactor == perturbedMaxBranchingFactor) {
        score += 0.1
      }
      else if (originalMaxBranchingFactor < perturbedMaxBranchingFactor) {
        score += 0.2
      }
      else {
        score += 0.2
      }


      if (originalMaxProperties == perturbedMaxProperties) {
        score += 0.1
      }
      else if (originalMaxProperties < perturbedMaxProperties) {
        score += 0.02
      }
      else {
        score += 0.02
      }


      if (originalStoredValue == perturbedStoredValue) {
        score += 0.1
      }
      else if (originalStoredValue < perturbedStoredValue) {
        score += 0.2
      }
      else {
        score += 0.2
      }

      val df = new DecimalFormat("#.##") // Format to two decimal places

      df.format(score).toDouble
    }


    // Apply the calculateSimRank function to each line
    val scoredData = data.map(line => (calculateSimRank(line), line))

    // Create a function to format the data with the message
    def formatData(score: Double, message: String): String = s"$score: $message"

    // Categorize data based on score
    val below_0_9 = scoredData.filter { case (score, _) => score < 0.9 }

    // Save categorized data with messages
//    below_0_9.map { case (score, line) => formatData(score, "This node was removed\nThis node was taken out") }
//      .saveAsTextFile("/Users/muzza/Desktop/professorFiles/simRankScore/below_0.9")


    val equal_to_0_9 = scoredData.filter { case (score, _) => score == 0.9 }
    val above_0_9 = scoredData.filter { case (score, _) => score > 0.9 }

    // Save categorized data
    below_0_9.values.saveAsTextFile("/Users/muzza/Desktop/professorFiles/simRankScore/below_0.9")
    equal_to_0_9.values.saveAsTextFile("/Users/muzza/Desktop/professorFiles/simRankScore/equal_to_0.9")
    above_0_9.values.saveAsTextFile("/Users/muzza/Desktop/professorFiles/simRankScore/above_0.9")

    // Stop the SparkContext
    sc.stop()
  }
}
