package test_analysis


import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import scala.collection.mutable.Stack
import domain.{CategoryTree, Category}

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
    categoryTree.root.addSubcategory(new Category("Science"))
    categoryTree.root.addSubcategory(new Category("Fiction"))
    categoryTree.root.addSubcategory(new Category("Documents"))
    categoryTree.root.subcategories("Science").addSubcategories(List("Physics",
      "Computer Science", "Math", "Biology"))
    categoryTree.root.subcategories("Fiction").addSubcategories(List("Fantasy", "Science-Fiction", "Horror"))
    categoryTree.root("Science")("Math").addSubcategory("Calculus")

    println(categoryTree)
    println(categoryTree.root("Fiction").namesOfSubcategories)
    assert(categoryTree.root("Fiction").namesOfSubcategories.size === 3)
  }
}