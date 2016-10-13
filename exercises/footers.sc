object footers {
	import scala.concurrent.ExecutionContext.Implicits.global
 	import scala.concurrent.Future
  
 	val result: Future[Int] = Future.successful(1)
                                                  //> result  : scala.concurrent.Future[Int] = scala.concurrent.impl.Promise$KeptP
                                                  //| romise@7dc7cbad
	val resultStr: Future[String] = result map { i => i.toString }
                                                  //> resultStr  : scala.concurrent.Future[String] = List()
  
  
  
}