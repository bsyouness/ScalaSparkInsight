sbt package
~/bin/spark-1.4.0/bin/spark-submit --class "SparkMedian" --master "local[4]" target/scala-2.10/tweetprocessing_2.10-0.1.0.jar ./tweet_input/tweets.txt ./tweet_output/ft2.txt
~/bin/spark-1.4.0/bin/spark-submit --class "WordCount" --master "local[4]" target/scala-2.10/tweetprocessing_2.10-0.1.0.jar ./tweet_input/tweets.txt ./tweet_output/ft1.txt