package domain

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter


class BookQueueTest extends FunSuite with BeforeAndAfter {

  def createSomePrioritizedBooks() = {
    val Joe = new Author("Joe")
    val Leonard = new Author("Leonard")
    val ScaryExperiments = new Book("Scary experiments", Joe)
    val BedtimeStories = new Book("Bedtime stories", Joe)
    val OldHistory = new Book("Old History", Leonard)
    val Electrodynamics = new Book("Electrodynamics", Leonard)

    List(
      new PrioritizedBook(ScaryExperiments, 3),
      new PrioritizedBook(BedtimeStories, 1),
      new PrioritizedBook(OldHistory, 1),
      new PrioritizedBook(Electrodynamics, 10)
    )
  }

  test("Add some books with priorities and check if they are in right order.") {
    val organizer = new BookOrganizer
    val books = createSomePrioritizedBooks()
    organizer createNewQueue books
    val orderedByPriorities = organizer.getAll()
    assert(orderedByPriorities(0) === books(3))
    assert(orderedByPriorities(1) === books(0))
  }

  test("Remove books from queue which didn't have biggest priority.") {
    val organizer = new BookOrganizer
    val books = createSomePrioritizedBooks()
    organizer createNewQueue books
    organizer removeBook books(0)
    organizer removeBook books(1)

    val orderedByPriorities = organizer.getAll()
    assert(orderedByPriorities.size == 2)
    assert(orderedByPriorities(0) === books(3))
    assert(orderedByPriorities(1) === books(2))
  }
}
