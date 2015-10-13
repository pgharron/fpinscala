object ErrorHandling {
  import fpinscala.errorhandling._
  import fpinscala.errorhandling.Option._

  val x = Some(1)                                 //> x  : fpinscala.errorhandling.Some[Int] = Some(1)

  x orElse Some(2)                                //> res0: fpinscala.errorhandling.Option[Int] = Some(1)

  val k = Some(2.6922222222222225).flatMap({ s =>
    println(s">> $s")
    Some(s + 1)
  })                                              //> >> 2.6922222222222225
                                                  //| k  : fpinscala.errorhandling.Option[Double] = Some(3.6922222222222225)

  val ds = List(1.3, 2.3, 4, 5, 5, 6)             //> ds  : List[Double] = List(1.3, 2.3, 4.0, 5.0, 5.0, 6.0)
  //val ds = List()
  mean(ds)                                        //> res1: fpinscala.errorhandling.Option[Double] = Some(3.9333333333333336)
  mean(ds).map(x => math.pow(x - 3.9333333333333336, 2))
                                                  //> res2: fpinscala.errorhandling.Option[Double] = Some(0.0)
  //x flatmap {x => println(x); Some(x)}
  variance(ds)                                    //> res3: fpinscala.errorhandling.Option[Double] = Some(2.6922222222222225)
  mean(ds) flatMap (m => mean(ds.map(x => math.pow(x - m, 2))))
                                                  //> res4: fpinscala.errorhandling.Option[Double] = Some(2.6922222222222225)

  //Exercise 4.4
  val dsO = List(Some(1.3), Some(2.3), Some(4), Some(5), Some(5), Some(6))
                                                  //> dsO  : List[fpinscala.errorhandling.Some[AnyVal]] = List(Some(1.3), Some(2.3
                                                  //| ), Some(4), Some(5), Some(5), Some(6))
  sequence(dsO)                                   //> res5: fpinscala.errorhandling.Option[List[AnyVal]] = Some(List(1.3, 2.3, 4, 
                                                  //| 5, 5, 6))
  sequence_1(dsO)                                 //> res6: fpinscala.errorhandling.Option[List[AnyVal]] = Some(List(1.3, 2.3, 4, 
                                                  //| 5, 5, 6))

  val dsON = List(Some(1.3), Some(2.3), Some(4), Some(5), Some(5), Some(6), None)
                                                  //> dsON  : List[Product with Serializable with fpinscala.errorhandling.Option[A
                                                  //| nyVal]] = List(Some(1.3), Some(2.3), Some(4), Some(5), Some(5), Some(6), Non
                                                  //| e)
  sequence(dsON)                                  //> res7: fpinscala.errorhandling.Option[List[AnyVal]] = None
  sequence_1(dsON)                                //> res8: fpinscala.errorhandling.Option[List[AnyVal]] = None

  List(1.3, 2.3, 4, 5, 5, 6) map { f => Some(f) } //> res9: List[fpinscala.errorhandling.Some[Double]] = List(Some(1.3), Some(2.3)
                                                  //| , Some(4.0), Some(5.0), Some(5.0), Some(6.0))
  
  //Exercise 4.5
  traversePh(ds){ f => Some(f.toInt) }            //> res10: fpinscala.errorhandling.Option[List[Int]] = Some(List(1, 2, 4, 5, 5, 
                                                  //| 6))
  traverse(ds){ f => Some(f.toInt) }              //> res11: fpinscala.errorhandling.Option[List[Int]] = Some(List(1, 2, 4, 5, 5, 
                                                  //| 6))
}