import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import java.io._

object SparkMedian {
    def main(args: Array[String]) {
        if (args.length != 2) {
            println("Usage is: median.scala <input_path> <output_path>")
            exit(-1)}

        val input_path = args(0)
        val output_path = args(1)

        // Initiate a SparkContext instance.
        val conf = new SparkConf().setAppName("Median calculation")
        val sc = new SparkContext(conf)
        
        // Load the input file as a Spark collection.
        val lines = sc.textFile(input_path, 1)
        println(lines.toLocalIterator.toList)
       
        val unique_wordcounts = lines.map(x => x.split(' ').toSet.size)
        
        // Write the list of words and their count to an output file
        val output_file = new File(output_path)
        val bw = new BufferedWriter(new FileWriter(output_file))
        println("Writing to: " + output_path)
        val my_it = unique_wordcounts.toLocalIterator.toStream
        for (m <- RunningMedian.running_median(my_it)) {
            println(m)
            bw.write(f"$m%5.5f \n")
        }
        bw.close()
        // Kill the SparkContext instance.
        sc.stop()
    }
}