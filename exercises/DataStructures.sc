object DataStructures {

  import fpinscala.datastructures._
  import fpinscala.datastructures.List._

  append(List[Int](1, 2, 3), List[String]("hh"))  //> res0: fpinscala.datastructures.List[Any] = Cons(1,Cons(2,Cons(3,Cons(hh,Nil)
                                                  //| )))
  // Exercise 3.2
  tail(List[Int](1, 2, 3, 4))                     //> res1: fpinscala.datastructures.List[Int] = Cons(2,Cons(3,Cons(4,Nil)))
  // List.tail(Nil)
  // Exercise 3.3
  setHead(List[Int](1, 2, 3, 4), 99)              //> res2: fpinscala.datastructures.List[Int] = Cons(99,Cons(2,Cons(3,Cons(4,Nil)
                                                  //| )))
  // Exercise 3.4
  drop(List[Int](1, 2, 3, 4), 2)                  //> res3: fpinscala.datastructures.List[Int] = Cons(3,Cons(4,Nil))

  // Exercise 3.5
  dropWhile(List[Int](1, 2, 3, 4))(x => x < 2)    //> res4: fpinscala.datastructures.List[Int] = Cons(2,Cons(3,Cons(4,Nil)))
  // Exercise 3.6
  init(List[Int](1, 2, 3, 4))                     //> res5: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Nil)))

  // Exercise 3.7

  // Exercise 3.8

  val xs = List[Int](1, 2, 3)                     //> xs  : fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Nil)))
  foldRight(xs, Nil: List[Int])(Cons(_, _))       //> res6: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Nil)))

  // Exercise 3.9
  length(xs)                                      //> res7: Int = 3

  reverse(List(1, 2, 3))                          //> res8: fpinscala.datastructures.List[Int] = Cons(3,Cons(2,Cons(1,Nil)))

  val l = List(List(1, 2, 3), List(4, 5))         //> l  : fpinscala.datastructures.List[fpinscala.datastructures.List[Int]] = Con
                                                  //| s(Cons(1,Cons(2,Cons(3,Nil))),Cons(Cons(4,Cons(5,Nil)),Nil))
  concat(l)                                       //> res9: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Cons(4,Cons(
                                                  //| 5,Nil)))))

  appendFl(List[Int](1, 2, 3), List[String]("hh"))//> res10: fpinscala.datastructures.List[Any] = Cons(1,Cons(2,Cons(3,Cons(hh,Nil
                                                  //| ))))


  //Exercise 3.16
  tf(List(1, 2, 3, 4, 5, 6, 7))                   //> res11: fpinscala.datastructures.List[Int] = Cons(2,Cons(3,Cons(4,Cons(5,Cons
                                                  //| (6,Cons(7,Cons(8,Nil)))))))

  //Exercise 3.17
  tfD(List(1.0d, 1.25d,1.09d))                    //> res12: fpinscala.datastructures.List[String] = Cons(1.0,Cons(1.25,Cons(1.09,
                                                  //| Nil)))
  //Exercise 3.18
  map[Int,Int](List[Int](1, 2, 3, 4, 5, 6, 7)){
  	a => a+1
  }                                               //> res13: fpinscala.datastructures.List[Int] = Cons(2,Cons(3,Cons(4,Cons(5,Cons
                                                  //| (6,Cons(7,Cons(8,Nil)))))))
  
  //Exercise 3.19
  filter(List[Int](1, 2, 3, 4, 5, 6, 7)) {
  	a => a <= 3
  }                                               //> res14: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Nil)))
  
  //Exercise 3.20
  flatMap(List(1,2,3))(i => List(i,i))            //> res15: fpinscala.datastructures.List[Int] = Cons(1,Cons(1,Cons(2,Cons(2,Con
                                                  //| s(3,Cons(3,Nil))))))
    
  //Exercise 3.21
  filterFlatMap(List[Int](1, 2, 3, 4, 5, 6, 7)) {
  	a => a <= 3
  }                                               //> res16: fpinscala.datastructures.List[Int] = Cons(1,Cons(2,Cons(3,Nil)))
        
  //Exercise 3.22
  twoLs(List(1,2,3), List(4,5,6))                 //> res17: fpinscala.datastructures.List[Int] = Cons(5,Cons(7,Cons(9,Nil)))
  
  //Exercise 3.23
  zipWith(List(1,2,3), List(1,3,-10)) {(a,b) => a-b}
                                                  //> res18: fpinscala.datastructures.List[Int] = Cons(0,Cons(-1,Cons(13,Nil)))
                 
  //Exercise 3.24
  hasSubsequence(List(1,2,3,4), List(3,4))        //> res19: Boolean = true
  

  
  
        
}