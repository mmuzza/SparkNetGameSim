
import NetGraphAlgebraDefs.{NetGraph, NodeObject}
import org.apache.hadoop.shaded.org.apache.commons.beanutils.BeanUtils.describe
import org.scalatest.matchers.must.Matchers
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.must.Matchers.{contain, include}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper


class Tests extends AnyFunSuite with Matchers {


  test("getRandomNode should return a valid NodeObject") {

    val samplePerturbedGraph: Option[NetGraph] = NetGraph.load("NetGameSimNetGraph_26-10-23-23-39-25.ngs.perturbed", "/Users/muzza/Desktop/projectTwo/TO_USE/")
    val sampleNetPerturbedGraph: NetGraph = samplePerturbedGraph.get

    // Call the getRandomNode function
    val randomNode = RandomWalkSparkApp.getRandomNode(sampleNetPerturbedGraph)

    // Add your assertions here to verify the result
    assert(randomNode != null, "Test passed: Random Node contains Node of type NodeObject")
  }



  test("calculateSimRank returns the expected score for equal input values") {
    val csvLine = "1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0"
    val score = SparkSimRank.calculateSimRank(csvLine)
    score shouldEqual 0.9 // Expected score for equal input values
    info("Test passed: calculateSimRank returns the expected score for equal input values")
  }

  test("checkAuthenticity should return a valid output message") {
    val csvLine = "0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,true,0.11,0.12,0.13,0.14,0.15,0.16,0.17,0.18,0.19,true"
    val expectedSubstring1 = "Successful"
    val expectedSubstring2 = "HoneyPot"

    val resultMessage = SparkAttackerAuthenticate.checkAuthenticity(csvLine) // Replace YourClass with your actual class name

    val resultString = resultMessage.toString // Convert to String

    resultString should include(expectedSubstring1)
    resultString should include(expectedSubstring2)

    info("Test passed: checkAuthenticity returned a valid output message.")
  }



  test("Testing calculateSimRank") {
    val csvLine = "1,10,20,3,4,5,6,7,8,18,19,28,29,30,31,32,33,34,35,20"
    val score = SparkSimRank.calculateSimRank(csvLine)
    score shouldEqual 0.7 // Expected score for equal input values
    info("Test passed: calculateSimRank returns the expected score for equal input values")
  }


  test("checkAuthenticity Must return valid output") {
    val csvLine = "0.10,0.20,0.30,0.40,0.50,0.62,0.91,0.8,0.9,false,0.11,0.12,0.13,0.14,0.15,0.16,0.17,0.18,0.19,false"
    val expectedSubstring1 = "Successful"
    val expectedSubstring2 = "HoneyPot"

    val resultMessage = SparkAttackerAuthenticate.checkAuthenticity(csvLine) // Replace YourClass with your actual class name

    val resultString = resultMessage.toString // Convert to String

    resultString should include(expectedSubstring1)
    resultString should include(expectedSubstring2)

    info("Test passed: checkAuthenticity returned a valid output message.")
  }

}
