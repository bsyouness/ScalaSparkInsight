sbt package
~/bin/spark-1.4.0/bin/spark-submit --class "SparkMedian" --master "local[4]" target/scala-2.10/sparkmedian_2.10-1.0.jar ./tweet_input/tweets.txt ./tweet_output/fttest.txt
