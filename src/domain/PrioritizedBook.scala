package domain

import java.util.Date


class PrioritizedBook( book: Book, prio: Int = 0, deadl : Date = new Date()) {
  require(prio >= 0)
  require(book != null)
  var priority = prio
  var deadline = deadl
  var progress = 0.0

  def title = book.getTitle

  override
  def toString(): String = {
    "priority = " + priority + "\n" +
    "deadline = " + deadline  + "\n" +
    "progress =" + progress   + "\n" +
    "title = " + book.getTitle + "\n"
  }

}
