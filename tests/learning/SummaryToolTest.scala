package learning

import analysis.SummaryTool

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import scala.collection.mutable.Stack

class SummaryToolTest extends FunSuite with BeforeAndAfter {

  var summaryTool: SummaryTool = _
  var content: String = ""

  before {
    summaryTool = new SummaryTool
    content =
      """Lior Degani, the Co-Founder and head of Marketing of Swayy, pinged me last week when I was in California to tell me about his startup and give me beta access. I heard his pitch and was skeptical.

    I went into Swayy to check it out, and when it asked for access to my Twitter and permission to tweet from my account, all I could think was, “If this thing spams my Twitter account I am going to bitch-slap him all over the Internet. I like bananas in a fridge when I come back home.


		The analytics side isn’t as interesting for me right now, but that could be due to the fact that I’ve barely been online since I came back from the US last weekend. The graphs also haven’t given me any particularly special insights as I can’t see which post got the actual feedback on the graph side (however there are numbers on the Timeline side.) This is a Beta though, and new features are being added and improved daily. I’m sure this is on the list. As they say, if you aren’t launching with something you’re embarrassed by, you’ve waited too long to launch.

    It was the suggested content that impressed me the most. The articles really are spot on – which is why I pinged Lior again to ask a few questions:

How do you choose the articles listed on the site? Is there an algorithm involved? And is there any IP?

Yes, we’re in the process of filing a patent for it. But basically the system works with a Natural Language Processing Engine. Actually, there are several parts for the content matching, but besides analyzing what topics the articles are talking about, we have machine learning algorithms that match you to the relevant suggested stuff. For example, if you shared an article about Zuck that got a good reaction from your followers, we might offer you another one about Kevin Systrom (just a simple example).

    Who came up with the idea for Swayy, and why? And what’s your business model?






    Our business model is a subscription model for extra social accounts (extra Facebook / Twitter, etc) and team collaboration.

    The idea was born from our day-to-day need to be active on social media, look for the best content to share with our followers, grow them, and measure what content works best.

    Who is on the team?
      		"""
  }
  test("how split sentences work") {
    val sentences = summaryTool.splitContentToSentences(content)

    assert(sentences.size != 0)
    assert(sentences(0) === "Lior Degani, the Co-Founder and head" +
      " of Marketing of Swayy, pinged me last week when I was in" +
      " California to tell me about his startup and" +
      " give me beta access")
  }

  test("how format sentences work") {
    val sentences = summaryTool.splitContentToSentences(content)
    val sentence = "        Lior Degani, the Co-Founder.  "
    val formattedSentence = summaryTool.formatSentence(sentence)

    assert(formattedSentence.size > 0)
    assert(formattedSentence == "Lior Degani, the Co-Founder.")
  }

  test("Whats going on with my summaryTool") {

    val bestSentence = "I like bananas"


    val sentencesMap = summaryTool.getSentecesRanks(content)

    assert(sentencesMap.size > 0)
  }

  test("What is a best sentence") {
    val title = "One day in the woods"
    val sentencesMap = summaryTool.getSentecesRanks(content)
    // Build the summary with the sentences dictionary
    val summary = summaryTool.getSummary(title, content, sentencesMap)


    println(summary)

    // Print the ratio between the summary length and the original length
    println()
    println("Original Length " + (title.size + content.size))
    println("Summary Length " + (summary.size))
    println("Summary Ratio: " + (100 - (100 * (summary.size / (title.size + content.size)))))

    assert(summary.size > 0)
    assert(summary.size < title.size + content.size)
  }


}