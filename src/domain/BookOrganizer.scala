package domain

import java.util.Date


class BookOrganizer {
  val q = new BookQueue
  def addBook(book: PrioritizedBook) {
    q.enqueue(book)
  }

  def changeBookPriority(book: PrioritizedBook, newPriority: Int) {
    require(newPriority >= 0)
    q.removeFromQueue(book)
    book.priority = newPriority
    q.enqueue(book)
  }

  def changeBookProgress(book: PrioritizedBook, newProgress: Double) {
    require(newProgress >= 0.0)
    q.removeFromQueue(book)
    book.progress = newProgress
    q.enqueue(book)
  }

  def changeBookDeadline(book: PrioritizedBook, deadline: Date) {
    q.removeFromQueue(book)
    book.deadline = deadline
    q.enqueue(book)
  }
  def get
  First(k: Int) = {
    q.getFirst(k)
  }

  def getAll() = q.getAll()
}
