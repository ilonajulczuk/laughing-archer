package analysis

import java.io.RandomAccessFile
import scala.collection.mutable.HashMap
import java.io.BufferedWriter
import java.io.BufferedReader
import java.io.{FileWriter, FileReader}


/**
 * CategoryClassifier classify text into categories using function
 * matchCategory. Matching categories is based on algorithm which uses
 * set intersection and already prepared vocabulary which is specific
 * to to different categories.
 */
object CategoryClassifier {

  def removePunctuation(text: String) = {
    val punct = ",.?;:!\""
    text.toList.filterNot(char => punct contains char).mkString("")
  }

  def getWordsFromRawText(text: String) = {
    removePunctuation(text).toLowerCase.split("\\s+").toList
  }

  def getAllWords(basePath: String) = {
    import java.io.File
    val baseDir = new File(basePath)
    val paths = baseDir.listFiles.toList.sorted
    var words = List[String]()
    for (path <- paths) {
      val raf = new RandomAccessFile(path, "r")
      val buff = new Array[Byte](raf.length.toInt)
      raf.readFully(buff)
      words = words ++: getWordsFromRawText(new String(buff))
    }
    words
  }

  /**
   * rankWords is used to rank words based on how many times they occur
   * in the given list of words if they aren't in the same time in the set of
   * normalVocabulary. It doesn't make sense to rank words like 'that', 'and'
   * because it doesn't produce any value.
   * @param words - list of words to rank
   * @param normalVocabulary  - list of words we want to exclude when counting
   * @return list of words sorted on how many times they occur
   */
  def rankWords(words: List[String], normalVocabulary: Set[String]) = {
    val wordOccurences = new HashMap[String, Int]()
    for (word <- words if !(normalVocabulary contains word)) {
      if (wordOccurences contains word) {
        wordOccurences(word) += 1
      }
      else
        wordOccurences(word) = 1
    }
    wordOccurences.toList.sortBy(_._2).reverse
  }

  val basePath = "/home/att/eclipse/MyProject/src/analysis/articles/"
  val commonPhysicsVoc = readWordsFromFile(basePath + "physicsVoc.txt")
  val commonElectronicsVoc = readWordsFromFile(basePath + "electronicsVoc.txt")
  val commonMathVoc = readWordsFromFile(basePath + "mathVoc.txt")
  val commonNormalVoc = readWordsFromFile(basePath + "normalVoc.txt")

  /**
   *
   * @param text to witch this function matches Category
   * @return tuple (name of category, percent of similarity)
   * */
  def matchCategory(text: String) = {
    val words = getWordsFromRawText(text).toSet
    val wordsCount = words.size
    val categories = HashMap("physics" -> (words intersect commonPhysicsVoc).size.toDouble / wordsCount * 100,
      "electronics" -> (words intersect commonElectronicsVoc).size.toDouble / wordsCount * 100,
      "math" -> (words intersect commonMathVoc).size.toDouble / wordsCount * 100).toList.sortBy(_._2).reverse

    val winningCategory = if (categories(0)._2 > 10)
      categories(0)
    else
      ("novel", (words intersect commonNormalVoc).size.toDouble / wordsCount * 100)
    winningCategory
  }

  def writeWordsToFile(setOfWords: Set[String], path: String) {
    val out = new BufferedWriter(new FileWriter(path))
    for (word <- setOfWords.toList) {
      out.write(word + " ")
    }
    out.write("\n")
    out.close()
  }

  def readWordsFromFile(path: String) = {
    val in = new BufferedReader(new FileReader(path))
    var wordsInText = ""
    var nextLine = in.readLine
    while (nextLine != null) {
      wordsInText += nextLine
      nextLine = in.readLine
    }
    in.close()
    wordsInText.split("\\s+").toSet

  }
}