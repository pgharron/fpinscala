object StateCH6 {
  import fpinscala.state._
  import fpinscala.state.RNG._

  val xx = Simple(2)                              //> xx  : fpinscala.state.RNG.Simple = Simple(2)
  
  xx.nextInt                                      //> res0: (Int, fpinscala.state.RNG) = (769497,Simple(50429807845))
  nonNegativeInt(xx)                              //> res1: (Int, fpinscala.state.RNG) = (769497,Simple(50429807845))
  
  double(xx)                                      //> res2: (Double, fpinscala.state.RNG) = (3.5832496359944344E-4,Simple(50429807
                                                  //| 845))
  doubleUsingMap(xx)                              //> res3: (Double, fpinscala.state.RNG) = (3.5832496359944344E-4,Simple(50429807
                                                  //| 845))
  intDouble(xx)                                   //> res4: ((Int, Double), fpinscala.state.RNG) = ((769497,0.9258419186808169),Si
                                                  //| mple(130300666313612))
        
        
  ints(6)(xx)                                     //> res5: (List[Int], fpinscala.state.RNG) = (List(769497, 1988230381, -12775714
                                                  //| 85, -1827708387, 962104480, -1559240709),Simple(179288577635664))
  
  val k = unit(4) { xx }                          //> k  : (Int, fpinscala.state.RNG) = (4,Simple(2))
  nonNegativeEven { xx }                          //> res6: (Int, fpinscala.state.RNG) = (769496,Simple(50429807845))
  randIntDouble { xx }                            //> res7: ((Int, Double), fpinscala.state.RNG) = ((769497,0.9258419186808169),Si
                                                  //| mple(130300666313612))
  sequence(List(unit(1), unit(2), unit(3)))(xx)._1//> res8: List[Int] = List(1, 2, 3)
  unit(4) { xx }                                  //> res9: (Int, fpinscala.state.RNG) = (4,Simple(2))
  rollDie(Simple(5))._1                           //> res10: Int = 1
  val f1 = nonNegativeLessThan(6)                 //> f1  : fpinscala.state.RNG.Rand[Int] = <function1>

f1(Simple(5))._1                                  //> res11: Int = 0


  
  map2(int, int)((a,b) => a * b)(xx)              //> res12: (Int, fpinscala.state.RNG) = (1243176421,Simple(130300666313612))
  
  
  val c = List(nonNegativeEven, nonNegativeEven, nonNegativeEven, nonNegativeEven)
                                                  //> c  : List[fpinscala.state.RNG.Rand[Int]] = List(<function1>, <function1>, <f
                                                  //| unction1>, <function1>)
  sequence(c){xx}                                 //> res13: (List[Int], fpinscala.state.RNG) = (List(769496, 1988230380, 12775714
                                                  //| 84, 1827708386),Simple(161694279895846))
}