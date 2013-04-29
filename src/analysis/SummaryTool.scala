/* Inspired by some python code:
 * 
 * # This is a naive text summarization algorithm
# Created by Shlomi Babluki
# April, 2013

*/

package analysis

import scala.collection.mutable.DoubleLinkedList
class SummaryTool {

	def splitContentToSentences(content: String) = {

		content.replaceAll("\n", ". ").split("\\. ").filterNot(x => x.matches("\\W+")).filterNot(x => x == "")


	}
        
	def splitContentToParagraphs(content: String) = {
		content.split("\n\n")
	}
        
	def sentencesIntersection(sent1: String, sent2: String) : Double = {
		
		//split the sentence into words/tokens
        val s1 = sent1.split(" ").toSet
        val s2 = sent2.split(" ").toSet
 
        // If there is not intersection, just return 0
        if( (s1.size + s2.size) == 0)
        	return 0
		
        // We normalize the result by the average number of words
        return (s1.intersect(s2).size / ((s1.size + s2.size) / 2.0))
		
	}
	
	def formatSentence(sentence: String): String = sentence.replaceAll("\\s+$", "").replaceAll("^\\s+", "")
        
 // Convert the content into a dictionary <K, V>
    // k = The formatted sentence
    // V = The rank of the sentence
    def getSentecesRanks(content: String) = {
        // Split the content into sentences
        val sentences = splitContentToSentences(content)
        
        import Array._
        // Calculate the intersection of every two sentences
        val n = sentences.size
        try {
        	
        	val values = ofDim[Double](n,n)

        	for (i <- 0 until n) {
        				var row = Array
        				for (j <- 0 until n) {
        					if(i != j)
        					values(i)(j) = sentencesIntersection(sentences(i), sentences(j))
        					else values(i)(j) = 0.0
        				}
        	}
        	var sentencesMap = Map[String, Double]()
        	for (i <- 0 until n) {
        		var score = 0.0
        		for (j <- 0 until n) {
        			if (i != j) {
        			score += values(i)(j)
        		}
        	}
        		sentencesMap += (formatSentence(sentences(i)) -> score)
        	}
        	sentencesMap
        }
        catch
        {
        	case e: Throwable => println ("Probably some problem with array " + e.getMessage)
        	Map("nothing" -> 0.0)
        }
        
        
        
    }
	
	def getBestSentence(paragraph: String, sentencesMap: Map[String, Double]): String = {
		val sentences = splitContentToSentences(paragraph)
		val n = sentences.size
		if (n < 2) {
			return ""
		}
		//Convert it using reduceleft
		var best_sentence = ""
        var max_value = 0.0
        for (s <- sentences) {
        	val strip_s = formatSentence(s)
            if( strip_s != "") {
            	if( sentencesMap(strip_s) > max_value)
            	{
            		max_value = sentencesMap(strip_s)
                    best_sentence = s
            	}
            }
        }
		best_sentence
	}
	
	def getSummary(title: String, content: String, sentencesMap: Map[String, Double]) = {
		val paragraphs = splitContentToParagraphs(content)
		var summary = List[String]()
        summary = summary :+ (title.replaceAll("\\s+$", ""))
        
        for( p <- paragraphs) {
        	val sentence = getBestSentence(p, sentencesMap)
            if( sentence != "") summary = summary :+ formatSentence(sentence)
        }
		summary.mkString("\n")
	}
	
	def main() {
 
    // Demo
    //Content from: "http://thenextweb.com/apps/2013/03/21/swayy-discover-curate-content/"
 
    val title = """
    Swayy is a beautiful new dashboard for discovering and curating online content [Invites]
    """
 
    val content = """
    Lior Degani, the Co-Founder and head of Marketing of Swayy, pinged me last week when I was in California to tell me about his startup and give me beta access. I heard his pitch and was skeptical. I was also tired, cranky and missing my kids – so my frame of mind wasn’t the most positive.
 
    I went into Swayy to check it out, and when it asked for access to my Twitter and permission to tweet from my account, all I could think was, “If this thing spams my Twitter account I am going to bitch-slap him all over the Internet.” Fortunately that thought stayed in my head, and not out of my mouth.
 
    One week later, I’m totally addicted to Swayy and glad I said nothing about the spam (it doesn’t send out spam tweets but I liked the line too much to not use it for this article). I pinged Lior on Facebook with a request for a beta access code for TNW readers. I also asked how soon can I write about it. It’s that good. Seriously. I use every content curation service online. It really is That Good.
 
    What is Swayy? It’s like Percolate and LinkedIn recommended articles, mixed with trending keywords for the topics you find interesting, combined with an analytics dashboard that shows the trends of what you do and how people react to it. I like it for the simplicity and accuracy of the content curation. Everything I’m actually interested in reading is in one place – I don’t have to skip from another major tech blog over to Harvard Business Review then hop over to another major tech or business blog. It’s all in there. And it has saved me So Much Time
 
 
 
    After I decided that I trusted the service, I added my Facebook and LinkedIn accounts. The content just got That Much Better. I can share from the service itself, but I generally prefer reading the actual post first – so I end up sharing it from the main link, using Swayy more as a service for discovery.
 
    I’m also finding myself checking out trending keywords more often (more often than never, which is how often I do it on Twitter.com).
 
 
 
    The analytics side isn’t as interesting for me right now, but that could be due to the fact that I’ve barely been online since I came back from the US last weekend. The graphs also haven’t given me any particularly special insights as I can’t see which post got the actual feedback on the graph side (however there are numbers on the Timeline side.) This is a Beta though, and new features are being added and improved daily. I’m sure this is on the list. As they say, if you aren’t launching with something you’re embarrassed by, you’ve waited too long to launch.
 
    It was the suggested content that impressed me the most. The articles really are spot on – which is why I pinged Lior again to ask a few questions:
 
    How do you choose the articles listed on the site? Is there an algorithm involved? And is there any IP?
 
    Yes, we’re in the process of filing a patent for it. But basically the system works with a Natural Language Processing Engine. Actually, there are several parts for the content matching, but besides analyzing what topics the articles are talking about, we have machine learning algorithms that match you to the relevant suggested stuff. For example, if you shared an article about Zuck that got a good reaction from your followers, we might offer you another one about Kevin Systrom (just a simple example).
 
    Who came up with the idea for Swayy, and why? And what’s your business model?
 
    Our business model is a subscription model for extra social accounts (extra Facebook / Twitter, etc) and team collaboration.
 
    The idea was born from our day-to-day need to be active on social media, look for the best content to share with our followers, grow them, and measure what content works best.
 
    Who is on the team?
 
    Ohad Frankfurt is the CEO, Shlomi Babluki is the CTO and Oz Katz does Product and Engineering, and I [Lior Degani] do Marketing. The four of us are the founders. Oz and I were in 8200 [an elite Israeli army unit] together. Emily Engelson does Community Management and Graphic Design.
 
    If you use Percolate or read LinkedIn’s recommended posts I think you’ll love Swayy.
 
    ➤ Want to try Swayy out without having to wait? Go to this secret URL and enter the promotion code thenextweb . The first 300 people to use the code will get access.
 
    Image credit: Thinkstock
 
    """
    
    val sentencesMap = getSentecesRanks(content)
 
    // Build the summary with the sentences dictionary
    val summary = getSummary(title, content, sentencesMap)
 
    
    println(summary)
 
    // Print the ratio between the summary length and the original length
    println() 
    println ("Original Length " + (title.size + content.size))
    println ("Summary Length " + (summary.size))
    println ("Summary Ratio: " + (100 - (100 * (summary.size / (title.size + content.size)))))
 
	}
}
