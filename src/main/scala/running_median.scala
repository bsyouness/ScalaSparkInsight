import scala.collection.mutable.PriorityQueue

object RunningMedian {
	def running_median_next(left_heap: PriorityQueue[Int], right_heap: PriorityQueue[Int], x: Int): 
	(PriorityQueue[Int], PriorityQueue[Int], Double) = {
		//add the new element to the appropriate heap
		if (right_heap.length == 0 || x >= -right_heap.head){
			right_heap.enqueue(-x)
		}else{
			left_heap.enqueue(x)}

		//ensure the lists are balanced
		if (right_heap.length < left_heap.length){
			right_heap.enqueue(-left_heap.dequeue)
		}else if (right_heap.length - left_heap.length == 2) {
			left_heap.enqueue(-right_heap.dequeue)
		}

		//calculate the median
		val median = if (right_heap.length > left_heap.length) {
			-right_heap.head
		}else{
			(left_heap.head - right_heap.head) / 2.0
		}

		(left_heap, right_heap, median)
	}

	def running_median (numbers: Stream[Int]): Stream[Double] = {
		//do I need to do this?
		var left_heap = PriorityQueue.empty[Int]
		var right_heap = PriorityQueue.empty[Int]
		var median: Double = 0
		for (n <- numbers) yield {
			// (left_heap, right_heap, median) = running_median_next(left_heap, right_heap, n) 
	    	val my_tuple = running_median_next(left_heap, right_heap, n)
	       	left_heap = my_tuple._1
	       	right_heap = my_tuple._2
	       	median = my_tuple._3
	       	median
	    }
    }
}