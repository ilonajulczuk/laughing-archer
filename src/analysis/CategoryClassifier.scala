package analysis

import java.io.RandomAccessFile
import scala.collection.mutable.HashMap
import java.io.BufferedWriter
import java.io.BufferedReader
import java.io.{FileWriter, FileReader}

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
		for( path <- paths) {
			val raf = new RandomAccessFile(path, "r")
			val buff = new Array[Byte](raf.length.toInt)
			raf.readFully(buff)
			words = words ++: getWordsFromRawText(new String(buff))
		}
		words
	}
	
	def rankWords(words: List[String], normalVocabulary: Set[String]) = {
		var wordOccurences = new HashMap[String, Int]()
		for(word <- words if !( normalVocabulary contains word) ){
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


	def matchCategory(text: String) = {
		val words = getWordsFromRawText(text).toSet
		val wordsCount = words.size
		val categories =  HashMap("physics" -> (words intersect commonPhysicsVoc).size.toDouble / wordsCount * 100,
		"electronics" -> (words intersect commonElectronicsVoc).size.toDouble / wordsCount * 100,
		"math" -> (words intersect commonMathVoc).size.toDouble / wordsCount * 100).toList.sortBy(_._2).reverse
		
		val winningCategory = if (categories(0)._2 > 10)
		 categories(0) 
		else 
			("boring", (words intersect commonNormalVoc).size.toDouble / wordsCount * 100)
		winningCategory
	}
	
	def writeWordsToFile(setOfWords: Set[String], path: String) {
		val out = new BufferedWriter(new FileWriter(path))
		for(word <- setOfWords.toList)
		{
			out.write(word + " ")
		}
		out.write("\n")
		out.close()
	}
	
	def readWordsFromFile(path: String) = {
		val in = new BufferedReader(new FileReader(path))
		var wordsInText = ""
		var nextLine = in.readLine
		while(nextLine != null)
		{
			wordsInText += nextLine
			nextLine = in.readLine
		}
		in.close()
		wordsInText.split("\\s+").toSet
	
	}
}