object Laziness {
  import fpinscala.laziness._
  import fpinscala.laziness.Stream._

  val x = Stream(1, 2, 3, 4, 5)                   //> x  : fpinscala.laziness.Stream[Int] = Cons(<function0>,<function0>)
  x.toList                                        //> res0: List[Int] = List(1, 2, 3, 4, 5)

  val y = x.take(2)                               //> y  : fpinscala.laziness.Stream[Int] = Cons(<function0>,<function0>)
  y.toList                                        //> res1: List[Int] = List(1, 2)

  x.takeWhile(x => x < 3).toList                  //> res2: List[Int] = List(1, 2)

  Stream(1, 2, 3, 4, 5).take(3).toList            //> res3: List[Int] = List(1, 2, 3)

  Stream(1, 2, 3, 4, 5).exists(a => a == 3)       //> res4: Boolean = true

  Stream(1, 2, 3, 4, 5).forAll(x => x > 0)        //> res5: Boolean = true

  x.takeWhile_1(x => x < 3).toList                //> res6: List[Int] = List(1, 2)

  Stream(1, 2, 3, 4, 5).headOption                //> res7: Option[Int] = Some(1)
  Empty.headOption                                //> res8: Option[Nothing] = None

  Stream(1, 2, 3, 4, 5).map {
    x => x + 1
  }.toList                                        //> res9: List[Int] = List(2, 3, 4, 5, 6)

  Stream(1, 2, 3, 4, 5).filter {
    x => x >= 3
  }.toList                                        //> res10: List[Int] = List(3, 4, 5)

  Stream(1, 2).append(Stream(5)).toList           //> res11: List[Int] = List(1, 2, 5)

  Empty.append(Stream(5)).toList                  //> res12: List[Int] = List(5)

  Empty.foldRight(Stream(5))((h, t) => cons(h, t)).toList
                                                  //> res13: List[Int] = List(5)

  Stream(1, 2).flatMap(a => Stream(a + 1)).toList //> res14: List[Int] = List(2, 3)

  Stream(1).flatMap(a => Stream(a + 1))           //> res15: fpinscala.laziness.Stream[Int] = Cons(<function0>,<function0>)
  //trace...
  //Stream ( 1,2) flatmap (a => Stream(a+1))
  //foldRight(empty) (1,2 => Stream(2) append Stream(2) foldRight((empty)(a => Stream(a+1)))
  //Stream(2) flatMap (a => Stream(a+1))
  //foldRight(empty)(s) => Stream(3)

  val ones = Stream.constant(1).take(19).toList   //> ones  : List[Int] = List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                                  //|  1, 1)
  Stream.from(1).take(10).toList                  //> res16: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  val twos = Stream.constant(2).take(5).toList    //> twos  : List[Int] = List(2, 2, 2, 2, 2)

  // Exercise 5.10
  val f = Stream.fibs.take(10).toList             //> f  : List[Int] = List(0, 1, 1, 2, 3, 5, 8, 13, 21, 34)

  // Exercise 5.11 & 12

  Stream.fromUsingUnfold(1).take(10).toList       //> res17: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

  val oneUnfold = Stream.onesUsingUnfold.take(8).toList
                                                  //> oneUnfold  : List[Int] = List(1, 1, 1, 1, 1, 1, 1, 1)
  // Exercise 5.13
  Stream(1, 2, 3, 4, 5).mapViaUnfold(f => f + 99).toList
                                                  //> res18: List[Int] = List(100, 101, 102, 103, 104)

  Stream(1, 2, 3, 4, 5).takeViaUnfold(2).toList   //> res19: List[Int] = List(1, 2)

  Stream.fromUsingUnfold(7).takeWhileViaUnfold(x => x < 16).toList
                                                  //> res20: List[Int] = List(7, 8, 9, 10, 11, 12, 13, 14, 15)

  Stream.fromUsingUnfold(99).zipWith(Stream.onesUsingUnfold)((a, b) => a + b).take(10).toList
                                                  //> res21: List[Int] = List(100, 101, 102, 103, 104, 105, 106, 107, 108, 109)

  Stream.fromUsingUnfold(99).zipWith(Stream(1))((a, b) => a + b).take(10).toList
                                                  //> res22: List[Int] = List(100)
  Stream.fromUsingUnfold(99).zipAll(Stream(1, 2, 3)).take(10).toList
                                                  //> res23: List[(Option[Int], Option[Int])] = List((Some(99),Some(1)), (Some(10
                                                  //| 0),Some(2)), (Some(101),Some(3)))
  Stream.fromUsingUnfold(99).zipWith(Stream(1))((a, b) => if (a == b) a).take(10).toList
                                                  //> res24: List[AnyVal] = List(())
  Stream.empty zipAll Stream.empty take (10) toList
                                                  //> res25: List[(Option[Nothing], Option[Nothing])] = List()

  Stream(1, 2, 3).startsWith(Stream(2))           //> res26: Boolean = false

  Stream(1, 2, 3).take(1).tails.toList            //> res27: List[fpinscala.laziness.Stream[Int]] = List(Cons(<function0>,<functi
                                                  //| on0>), Empty)

  Stream(1, 2, 3).tails.toList.map(c => c.toList) //> res28: List[List[Int]] = List(List(1, 2, 3), List(2, 3), List(3), List())
  Stream(1, 2, 3).hasSubSequence(Stream(2, 3))    //> res29: Boolean = true

  Stream(1, 2, 3).scanRight(0)(_ + _).toList      //> List(0)
                                                  //| b2=3 p1=0,Some(0) 
                                                  //| List(3, 0)
                                                  //| List(0)
                                                  //| b2=3 p1=0,Some(0) 
                                                  //| b2=5 p1=3,Some(3) 
                                                  //| List(5, 3, 0)
                                                  //| List(0)
                                                  //| b2=3 p1=0,Some(0) 
                                                  //| List(3, 0)
                                                  //| List(0)
                                                  //| b2=3 p1=0,Some(0) 
                                                  //| b2=5 p1=3,Some(3) 
                                                  //| b2=6 p1=5,Some(5) 
                                                  //| res30: List[Int] = List(6, 5, 3, 0)

  val ones1 = Stream.constant(1)                  //> ones1  : fpinscala.laziness.Stream[Int] = Cons(<function0>,<function0>)
  ones1.forAll(_ != 1)                            //> res31: Boolean = false
}