package domain

import scala.collection.mutable.PriorityQueue


class BookQueue {

  def biggestPriority(book: PrioritizedBook) = book.priority
  var queue = new PriorityQueue[PrioritizedBook]()(Ordering.by(biggestPriority))
  def enqueue (book: PrioritizedBook) {
    queue.enqueue(book)
  }

  def dequeue () = queue.dequeue()

  def getFirst(k: Int) = {
     queue.toList.slice(0, k)
  }

  def getAll() = queue.toList

  def removeFromQueue(book: PrioritizedBook)  {
    queue = queue.filterNot(b => b == book)
  }

  def isEmpty() = queue.isEmpty

  def nonEmpty() = queue.nonEmpty

  def dropAll() {
    queue.clear()
  }
}
