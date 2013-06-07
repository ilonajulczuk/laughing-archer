package hello



class BookInfoUtility {
  val supportedBookFormats = List("mobi", "bin", "odt", "txt")
  val fileSeparator = "/"

  def extractLastWordFromPath(path: String) = {
    path.split(fileSeparator).last
  }

  def extractFormatFromPath(path: String) = {
    extractLastWordFromPath(path).split("\\.").last
  }

  def extractTitleFromPath(path: String) = {
    extractLastWordFromPath(path).split("\\.").head
  }

  def isFormatSupported(format: String) = {
    supportedBookFormats contains format
  }
}
