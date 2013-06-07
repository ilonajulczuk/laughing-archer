package domain

import java.util.Date


class PrioritizedBook(val book: Book, prio: Int = 0, deadl : Date = new Date()){
  require(prio >= 0)
  require(book != null)
  var priority = prio
  var deadline = deadl
  var progress = 0.0
  var id = -1
  def title = book.getTitle

  def equals(that: PrioritizedBook): Boolean = {
    if (that.priority != priority)  return false
    if (that.title != title) return false
    if(that.progress != progress) false
    else true
  }

  override def hashCode: Int = {
    var result: Int = title.hashCode
    result = 31 * result + priority.hashCode
    result = 31 * result + deadline.hashCode
    result = 31 * result + progress.hashCode
    result = 31 * result + title.hashCode
    return result
  }
  override
  def toString(): String = {
    "priority = " + priority + "\n" +
    "deadline = " + deadline  + "\n" +
    "progress =" + progress   + "\n" +
    "title = " + book.getTitle + "\n"
  }

}
