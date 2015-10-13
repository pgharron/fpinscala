object DataStructuresTree {

  import fpinscala.datastructures._
  import fpinscala.datastructures.Tree._

  //Exercise 3.25
  val t = Branch[Int](Leaf(1), Leaf(2))           //> t  : fpinscala.datastructures.Branch[Int] = Branch(Leaf(1),Leaf(2))
  size(t)                                         //> res0: Int = 3

  //Exercise 3.26
  maximum(Branch[Int](Branch(Leaf(1), Leaf(223)), Branch(Leaf(3), Leaf(99))))
                                                  //> res1: Int = 223

  //Exercise 3.27
  depth(Branch[Int](Branch(Leaf(1), Leaf(223)), Branch(Leaf(3), Branch(Branch(Leaf(3), Branch(Leaf(3), Leaf(99))), Leaf(99)))))
                                                  //> res2: Int = 5
//Branch[Int](Branch(Leaf(1), Leaf(223))
	//Exercise 3.28
	map[Int,Int](Branch[Int](Leaf(1), Leaf(223))){
		a => a+1
	}                                         //> res3: fpinscala.datastructures.Tree[Int] = Branch(Leaf(2),Leaf(224))

  //Exercise 3.29
	sizeViaFold(t)                            //> res4: Int = 3

  maximumViaFold(Branch[Int](Branch(Leaf(1), Leaf(223)), Branch(Leaf(3), Leaf(99))))
                                                  //> res5: Int = 223

  depthViaFold(Branch[Int](Branch(Leaf(1), Leaf(223)), Branch(Leaf(3), Branch(Branch(Leaf(3), Branch(Leaf(3), Leaf(99))), Leaf(99)))))
                                                  //> res6: Int = 5

	mapViaFold[Int,Int](Branch[Int](Leaf(1), Leaf(223))){
		a => a+1
	}                                         //> res7: fpinscala.datastructures.Tree[Int] = Branch(Leaf(2),Leaf(224))

}