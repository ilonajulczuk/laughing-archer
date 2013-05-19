import java.io.RandomAccessFile
import scala.collection.mutable.HashMap

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

val basePath = "articles/physics/"
val normalVocabulary = getAllWords("articles/random/")


val commonNormalVoc = (for {word <- rankWords(normalVocabulary, Set("")).slice(0, 400)} yield word._1).toSet

val physicsVocabulary = getAllWords("articles/physics/")
val commonPhysicsVoc = (for {word <- rankWords(physicsVocabulary, commonNormalVoc).slice(0, 400)} yield word._1).toSet

val electronicsVocabulary = getAllWords("articles/electronics/")
val commonElectronicsVoc = (for {word <- rankWords(electronicsVocabulary, commonNormalVoc).slice(0, 400)} yield word._1).toSet

val mathVocabulary = getAllWords("articles/math/")
val commonMathVoc = (for {word <- rankWords(mathVocabulary, commonNormalVoc).slice(0, 400)} yield word._1).toSet

val testArticleAboutPhysics = """
Thermodynamics is a branch of natural science concerned with heat and its relation to energy and work. It defines macroscopic variables (such as temperature, internal energy, entropy, and pressure) that characterize materials and radiation, and explains how they are related and by what laws they change with time. Thermodynamics describes the average behavior of very large numbers of microscopic constituents, and its laws can be derived from statistical mechanics.
Thermodynamics applies to a wide variety of topics in science and engineering—such as engines, phase transitions, chemical reactions, transport phenomena, and even black holes. Results of thermodynamic calculations are essential for other fields of physics and for chemistry, chemical engineering, aerospace engineering, mechanical engineering, cell biology, biomedical engineering, and materials science—and useful in other fields such as economics.[1][2]
Much of the empirical content of thermodynamics is contained in the four laws. The first law asserts the existence of a quantity called the internal energy of a system, which is distinguishable from the kinetic energy of bulk movement of the system and from its potential energy with respect to its surroundings. The first law distinguishes transfers of energy between closed systems as heat and as work.[3][4][5] The second law concerns two quantities called temperature and entropy. Entropy expresses the limitations, arising from what is known as irreversibility, on the amount of thermodynamic work that can be delivered to an external system by a thermodynamic process.[6] Temperature, whose properties are also partially described by the zeroth law of thermodynamics, quantifies the direction of energy flow as heat between two systems in thermal contact and quantifies the common-sense notions of "hot" and "cold".
Historically, thermodynamics developed out of a desire to increase the efficiency of early steam engines, particularly through the work of French physicist Nicolas Léonard Sadi Carnot (1824) who believed that the efficiency of heat engines was the key that could help France win the Napoleonic Wars.[7] Irish-born British physicist Lord Kelvin was the first to formulate a concise definition of thermodynamics in 1854:[8]
Thermo-dynamics is the subject of the relation of heat to forces acting between contiguous parts of bodies, and the relation of heat to electrical agency.
Initially, the thermodynamics of heat engines concerned mainly the thermal properties of their 'working materials', such as steam. This concern was then linked to the study of energy transfers in chemical processes, for example to the investigation, published in 1840, of the heats of chemical reactions[9] by Germain Hess, which was not originally explicitly concerned with the relation between energy exchanges by heat and work. Chemical thermodynamics studies the role of entropy in chemical reactions.[10][11][12][13][14][15][16][17][18] Also, statistical thermodynamics, or statistical mechanics, gave explanations of macroscopic thermodynamics by statistical predictions of the collective motion of particles based on the mechanics of their microscopic behavior.
"""

val testArticleAboutMath = """
Algebra can essentially be considered as doing computations similar to that of arithmetic with non-numerical mathematical objects.[1] Initially, these objects were variables that either represented numbers that were not yet known (unknowns) or represented an unspecified number (indeterminate or parameter), allowing one to state and prove properties that are true no matter which numbers are substituted for the indeterminates. For example, in the quadratic equation

 are indeterminates and  is the unknown. Solving this equation amounts to computing with the variables to express the unknowns in terms of the indeterminates. Then, substituting any numbers for the indeterminates, gives the solution of a particular equation after a simple arithmetic computation.
As it developed, algebra was extended to other non-numerical objects, like vectors, matrices or polynomials. Then, the structural properties of these non-numerical objects were abstracted to define algebraic structures like groups, rings, fields and algebras.
Before the 16th century, mathematics was divided into only two subfields, arithmetic and geometry. Even though some methods, which had been developed much earlier, may be considered nowadays as algebra, the emergence of algebra and, soon thereafter, of infinitesimal calculus as subfields of mathematics only dates from 16th or 17th century. From the second half of 19th century on, many new fields of mathematics appeared, some of them included in algebra, either totally or partially.
It follows that algebra, instead of being a true branch of mathematics, appears nowadays, to be a collection of branches sharing common methods. This is clearly seen in the Mathematics Subject Classification [2] where none of the first level areas (two digit entries) is called algebra. In fact, algebra is, roughly speaking, the union of sections 08-General algebraic systems, 12-Field theory and polynomials, 13-Commutative algebra, 15-Linear and multilinear algebra; matrix theory, 16-Associative rings and algebras, 17-Nonassociative rings and algebras, 18-Category theory; homological algebra, 19-K-theory and 20-Group theory. Some other first level areas may be considered to belong partially to algebra, like 11-Number theory (mainly for algebraic number theory) and 14-Algebraic geometry.
Elementary algebra is the part of algebra that is usually taught in elementary courses of mathematics.
Abstract algebra is a name usually given to the study of the algebraic structures themselves.

Elementary algebra is the most basic form of algebra. It is taught to students who are presumed to have no knowledge of mathematics beyond the basic principles of arithmetic. In arithmetic, only numbers and their arithmetical operations (such as +, −, ×, ÷) occur. In algebra, numbers are often denoted by symbols (such as a, n, x, y or z). This is useful because:
It allows the general formulation of arithmetical laws (such as a + b = b + a for all a and b), and thus is the first step to a systematic exploration of the properties of the real number system.
It allows the reference to "unknown" numbers, the formulation of equations and the study of how to solve these. (For instance, "Find a number x such that 3x + 1 = 10" or going a bit further "Find a number x such that ax + b = c". This step leads to the conclusion that it is not the nature of the specific numbers that allows us to solve it, but that of the operations involved.)
It allows the formulation of functional relationships. (For instance, "If you sell x tickets, then your profit will be 3x − 10 dollars, or f(x) = 3x − 10, where f is the function, and x is the number to which the function is applied".)
Polynomials


The graph of a polynomial function of degree 3.
Main article: Polynomial
A polynomial is an expression that is the sum of a finite number of non-zero terms, each term consisting of the product of a constant and a finite number of variables raised to whole number powers. For example, x2 + 2x − 3 is a polynomial in the single variable x. A polynomial expression is an expression that may be rewritten as a polynomial, by using commutativity, associativity and distributivity of addition and multiplication. For example, (x − 1)(x + 3) is a polynomial expression, that, properly speaking, is not a polynomial. A polynomial function is a function that is defined by a polynomial, or, equivalently, by a polynomial expression. The two preceding examples define the same polynomial function.
Two important and related problems in algebra are the factorization of polynomials, that is, expressing a given polynomial as a product of other polynomials that can not be factored any further, and the computation of polynomial greatest common divisors. The example polynomial above can be factored as (x − 1)(x + 3). A related class of problems is finding algebraic expressions for the roots of a polynomial in a single variable.
"""

val testArticleAboutElectronics = """
Wheatstone bridge
From Wikipedia, the free encyclopedia


Wheatstone bridge circuit diagram.
A Wheatstone bridge is an electrical circuit used to measure an unknown electrical resistance by balancing two legs of a bridge circuit, one leg of which includes the unknown component. Its operation is similar to the original potentiometer. It was invented by Samuel Hunter Christie in 1833 and improved and popularized by Sir Charles Wheatstone in 1843. One of the Wheatstone bridge's initial uses was for the purpose of soils analysis and comparison.[1]
Contents  [hide] 
1 Operation
2 Derivation
3 Significance
4 Modifications of the fundamental bridge
5 See also
6 References
7 External links
Operation [edit]

In the figure,  is the unknown resistance to be measured; ,  and  are resistors of known resistance and the resistance of  is adjustable. If the ratio of the two resistances in the known leg  is equal to the ratio of the two in the unknown leg , then the voltage between the two midpoints (B and D) will be zero and no current will flow through the galvanometer . If the bridge is unbalanced, the direction of the current indicates whether  is too high or too low.  is varied until there is no current through the galvanometer, which then reads zero.
Detecting zero current with a galvanometer can be done to extremely high accuracy. Therefore, if ,  and  are known to high precision, then  can be measured to high precision. Very small changes in  disrupt the balance and are readily detected.
At the point of balance, the ratio of

Alternatively, if , , and  are known, but  is not adjustable, the voltage difference across or current flow through the meter can be used to calculate the value of , using Kirchhoff's circuit laws (also known as Kirchhoff's rules). This setup is frequently used in strain gauge and resistance thermometer measurements, as it is usually faster to read a voltage level off a meter than to adjust a resistance to zero the voltage.
"""

val typicalGuardianArticle = """
The French president, François Hollande, has signed a law authorising same-sex couples to marry and adopt children, after months of street protests, political slanging matches and a rise in homophobic attacks.

The move makes France the ninth country in Europe and the 14th globally to legalise same-sex marriage.

France's official journal announced on Saturday that the bill had become law after the Constitutional Council rejected a challenge by the rightwing opposition on Friday.

The first same-sex marriage is due to be held in Montpellier in the south of France on 29 May, Reuters reported.

Hollande and his ruling Socialist party have made the legislation their flagship social change, but the right to marriage and adoption for everyone regardless of sexual orientation has triggered the biggest conservative and rightwing street protests in 30 years, with more than 200 arrests. Opponents have called for another protest on 26 May.

While French opinion polls have long shown that a majority of the public support same-sex marriage, the issue of adoption is more controversial.

The law also leaves key issues on family rights unanswered. It will not grant automatic co-parenting rights for same-sex couples in civil partnerships, nor allow access to medically assisted procreation or IVF to lesbian couples. Rights campaigners want these issues to be addressed in a family law this year.

The government has referred the issue of medically assisted procreation to France's national ethics council, which will rule in the autumn. But the issue of parenting and procreation rights remains deeply divisive in opinion polls and among politicians.

The other 13 countries to legalise same-sex marriage include Canada, Denmark, Sweden and most recently Uruguay and New Zealand. In the US, Washington DC and 12 states have legalised same-sex marriage.
"""

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
	println("Article matches category: " + winningCategory)
	println(categories)
	println("Similarity to normal vocabulary: " + (words intersect commonNormalVoc).size.toDouble / wordsCount * 100)
}

println("Testing article about physics:")
matchCategory(testArticleAboutPhysics)
println("\nTesting article about math:")
matchCategory(testArticleAboutMath)
println("\nTesting article about electronics:")
matchCategory(testArticleAboutElectronics)
println("\nTesting typical guardian article:")
matchCategory(typicalGuardianArticle)