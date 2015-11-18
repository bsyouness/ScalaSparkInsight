/* This is an executable for computing a running median. 
This code is based on this discussion:
http://stackoverflow.com/questions/10657503/find-running-median-from-a-stream-of-integers
It works by maintaining two heaps, one for the elements smaller than the median and one for
the elements larger than the median. The complexity for running the algorithm over n elements 
is O(nlog(n)).
*/

import scala.collection.mutable.PriorityQueue

object RunningMedian {
  def runningMedianNext(leftHeap: PriorityQueue[Int], rightHeap: PriorityQueue[Int], x: Int): (PriorityQueue[Int], PriorityQueue[Int], Double) = {
    /* This computes the next step in the running median computation. 
    We assume three invariants:
    1) The leftHeap's elements are all smaller than or equal to the rightHeap's,
    2) The numbers of elements in the heaps differ by at most one. We call this "balanced".
    3) rightHeap contains at least as many elements as leftHeap.
    Args:
      leftHeap: A max heap of numbers.
      rightHeap: A min heap of numbers.
      x: The next number to consider in the running median computation. 
    Returns:
      A tuple containing updated heaps and the new median.
    */

    // Add the new element to the appropriate heap.
    if (rightHeap.length == 0 || x >= -rightHeap.head) {
      rightHeap.enqueue(-x)
    } else {
      leftHeap.enqueue(x)
    }

    // Ensure the heaps are balanced.
    if (rightHeap.length < leftHeap.length) {
      rightHeap.enqueue(-leftHeap.dequeue)
    } else if (rightHeap.length - leftHeap.length == 2) {
      leftHeap.enqueue(-rightHeap.dequeue)
    }

    // Calculate the median.
    val median = if (rightHeap.length > leftHeap.length) {
      -rightHeap.head
    } else {
      (leftHeap.head - rightHeap.head) / 2.0
    }

    (leftHeap, rightHeap, median)
  }

  def runningMedian(numbers: Stream[Int]): Stream[Double] = {
    /* This takes in a stream of numbers and returns a stream which is the running median.
    Args:
      numbers: A stream of numbers.
    Returns:
      This returns a stream which is the running median.
    */
    val leftHeap = PriorityQueue.empty[Int]
    val rightHeap = PriorityQueue.empty[Int]
    val median: Double = 0
    // Scan the stream numbers, find new median using runningMedianNext, get the 3rd element of the stream of tuples, and get rid
    // of the first element of the stream (median: Double = 0)
    numbers.scanLeft(leftHeap, rightHeap, median)((medianTuple: (PriorityQueue[Int], PriorityQueue[Int], Double), x: Int) =>
      runningMedianNext(medianTuple._1, medianTuple._2, x)).map(_._3).tail
  }
}