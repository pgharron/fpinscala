object ErrorHandlingEither {
  import fpinscala.errorhandling._
  import fpinscala.errorhandling.Either._

  Try(1 / 0)                                      //> res0: fpinscala.errorhandling.Either[Exception,Int] = Left(java.lang.Arithme
                                                  //| ticException: / by zero)

  //Exercise 4.6
  val a = Right(10)                               //> a  : fpinscala.errorhandling.Right[Int] = Right(10)
  val b = Left(new Exception("ss"))               //> b  : fpinscala.errorhandling.Left[Exception] = Left(java.lang.Exception: ss)
                                                  //| 
  a.map2Ph(Right(1))((a, b) => a + b)             //> res1: fpinscala.errorhandling.Either[Nothing,Int] = Right(11)
	
	
	//Exercise 4.7
	sequence(List(a))                         //> res2: fpinscala.errorhandling.Either[Nothing,List[Int]] = Right(List(10))
	
}