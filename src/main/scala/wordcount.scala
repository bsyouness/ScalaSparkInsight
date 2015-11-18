/* This is an executable for counting words in Spark.
This is nearly identical to code in the Spark example library:
https://spark.apache.org/examples.html
*/

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import java.io._

object WordCountApp {
  def main(args: Array[String]) {
    /* Computes a histogram of word occurences from a set of tweets.

    Args: 
      inputPath: the name of the file to read, 
      output_path: the name of the file to output to.
    */

    if (args.length != 2) {
      println("Usage is: wordcount.scala <inputPath> <output_path>")
      exit(-1)
    }

    val inputPath = args(0)
    val output_path = args(1)

    // Initiate a SparkContext instance.
    val conf = new SparkConf().setAppName("Word count")
    val sc = new SparkContext(conf)

    // Load the input file as a Spark collection.
    val lines = sc.textFile(inputPath, 1)
    println(lines)
    // This is a map-reduce.
    // The map tokenizes the tweets into words and initializes counts for each word.
    // The reduce sums the counts, producing a histogram.
    // The histogram in the Insight example was sorted alphabetically, so we're doing a sort here.
    val counts = lines.flatMap(x => x.split(' '))
      .map(x => (x, 1))
      .reduceByKey(_ + _)
      .sortByKey()

    // Convert the output to a list.
    val output: List[(String, Int)] = counts.collect().toList
    println(output)
    // Write the list of words and their count to an output file
    val output_file = new File(output_path)
    val bw = new BufferedWriter(new FileWriter(output_file))
    println("Writing to: " + output_path)
    for ((word, count) <- output) {
      bw.write(f"$word%50s $count%50d \n")
    }
    bw.close()
    // Kill the SparkContext instance.
    sc.stop()
  }
}
