**Description: Spark For Net Game Sim**

**Name: Muhammad Muzzammil**


Instructions:

Begin by setting up your development environment, which includes installing IntelliJ, JDK, Scala runtime, the IntelliJ Scala plugin, and the Simple Build Toolkit (SBT). Ensure that you have also configured Scala monitoring tools for proper functionality.

Make sure to be using SDK 1.8 and Scala version of 2.13.10 as Spark is only compatible with that specific version.

Once the environment is set up, launch IntelliJ and open the project. Initiate the project build process. Please be patient as this step may take some time, as it involves downloading and incorporating the library dependencies listed in the build.sbt file into the project's classpath.

If you prefer an alternative approach, you can run the project via the command line. Open a terminal and navigate to the project directory for this purpose. The package includes one main class, one map reducer class for nodes, one map reducer class for edges, and one sharding class. Everything is ran through the “sbt clean compile assembly” command in the terminal.

You should use the original ReadMe file to generate the NGS file for both Original Graph and Perturbed Graph.

Before running the command, you must go to “Main.Scala” which can be found under source>main> and change the following directories to your computer directory to ensure running the program smoothly:
val Directory (This one is Directory for where your original and perturbed ngs files are located) --> This is used by storingFiles & RandomWalkSparkApp Classes

val originalGraphFileName (Name of the original graph file that you saved in your directory when you generated a graph from NetGameSim) --> This is used by storingFiles Class
val originalNodesCsvPath (This is the output directory of where you would like to store the Original Graph Nodes a txt file) --> This is used by storingFiles Class
val perturbedFileFromRandomWalkPath (This is the output file that was produced by RandomWalkSparkApp Class using the perturbed Graph) --> This is used by storingFiles Class
val perturbedXoriginalPath (Set this to the output directory you would like to produce the final csv file with cartesian product of perturbed X original nodes) --> This is used by storingFiles Class

val perturbedGraphFileName (Name of the perturbed graph file that you saved in your directory when you generated a graph from NetGameSim) --> This is used by RandomWalkSparkApp Class
val randomWalkOutputPath (This is set to be the output directory for random walk performed on perturbed graph) --> This is used by RandomWalkSparkApp Class

// Used by SparkAttackerAuthenticity Class:
val calculateStatsInputFile (This needs to be set to the output file produce by the SparkSimRank class) --> This is used by SparkAttackerAuthenticity Class
val statsOutput (This needs to be set to the directory of where you would like to produce the final stats of the algorithm) --> This is used by SparkAttackerAuthenticity Class

Make sure to add the netmodelsim.jar from professor's original project. You can do so by going to File > Project Structure > Modules > Dependencies > + > One Jar > *select* netmodelsim.jar > apply > *click* OK.

After these has been set, the program is ready to run
You can run the Spark Job in two ways:
First being locally
    You will have to configure the files in the RandomWalkSparkApp, SparkAttackerAutheticate and SparkSimRank
    Make sure you have Spark installed on your computer. You can make sure by going into your terminal and typing "spark-shell"

Second being on AWS
    You will have to create the jar file for the main class you want to run
    For that please set one of the map reduce classes as main in the sbt file
    Then press the command sbt clean compile assembly
    That will generate the jar file ProjectTwo.jar
    Use that file to upload to AWS and the files saved in your output directory as the input for s3 bucket which you produced running the RandomWalkSparkApp.scala

This project has a total of 5 tests in Scala. In order to run them using the terminal, cd into the project directory and run the command sbt clean compile test

