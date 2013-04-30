package test_analysis


import analysis.{CategoryTree, Node, Leaf, MultiBranch}

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import scala.collection.mutable.Stack
 
class CategoryTest extends FunSuite with BeforeAndAfter {

	var categoryTree: CategoryTree = _
	var content: String = ""

  before {
	categoryTree = new CategoryTree()
	}

	test("Can I access the elements of tree") {
	  val rootValue = categoryTree.root.value
	  assert(rootValue === "Root")
  }
	
	test("Test building a tree") {
		categoryTree.root.addChild(new MultiBranch("Science"))
		categoryTree.root.addChild(new MultiBranch("Fiction"))
		categoryTree.root.addChild(new MultiBranch("Documents"))
		categoryTree.root.children("Science").addChildren(List("Physics",
				"Computer Science", "Math", "Biology"))
		categoryTree.root.children("Fiction").addChildren(List("Fantasy", "Science-Fiction", "Horror"))	
		categoryTree.root("Science")("Math").addChild("Calculus")
		
		println(categoryTree)
		println(categoryTree.root("Fiction").namesOfChildren)
		assert(categoryTree.root("Fiction").namesOfChildren.size === 3)
  }
}