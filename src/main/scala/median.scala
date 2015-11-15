/* Library and executable for computing a running median using Spark.
*/

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import java.io._

object SparkMedian {
  def main(args: Array[String]) {
    /* Computes the running median of unique word counts in an input file simulating a tweet stream.
    The computation is broken into two parts. The first is a parallel map which pre-processes the 
    tweets. The second is a serial, streaming, running median.
    Args:
      inputPath: The path containing the tweets, with one tweet per line.
      outputPath: The path where the running median should be written.
    */

    if (args.length != 2) {
      println("Usage is: median.scala <inputPath> <outputPath>")
      exit(-1)
    }

    val inputPath = args(0)
    val outputPath = args(1)

    // Initiate a SparkContext instance.
    val conf = new SparkConf().setAppName("Median calculation")
    val sc = new SparkContext(conf)

    // Load the input file as a Spark collection.
    val lines = sc.textFile(inputPath, 1)

    // We preprocess the tweets in parallel using a map.
    // The result is a collection of `Int`s, counting the number of unique words in each tweet.
    val uniqueWordcounts = lines.map(x => x.split(' ').toSet.size)

    // The actual median computation appears to be inescabably serial, so we compute a running median locally,
    // using the iterator `unique_wordcounts.toLocalIterator()` to make things faster and limit memory usage.
    // NOTE: This merely streams the data through the local process; the data is not all loaded simultaneously.
    // NOTE: We are assuming the ordering of the tweets has been preserved throughout the Spark computation,
    // which is the case in the tests I ran. If this is not the case in general, we could augment the collections
    // with line numbers as keys, and use a `sortByKey` to recover the original order.
    val output_file = new File(outputPath)
    val bw = new BufferedWriter(new FileWriter(output_file))
    for (m <- RunningMedian.runningMedian(uniqueWordcounts.toLocalIterator.toStream)) {
      bw.write(f"$m%5.5f \n")
    }
    bw.close()

    // Kill the SparkContext instance.
    sc.stop()
  }
}