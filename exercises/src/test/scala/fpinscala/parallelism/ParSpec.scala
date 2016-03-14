package fpinscala.parallelism

import fpinscala.parallelism.Par._
import org.scalatest.FunSpec
import java.util.concurrent._
import org.scalatest.concurrent.Eventually._

class ParSpec extends FunSpec {

  describe("The Par mechanism") {

    it("should create a Future given a timeout") {

      val a = lazyUnit(18)
      val b = lazyUnit(6)

      val f: Par[Int] = Par.map2[Int, Int, Int](a, b) {
        case (x, y) => x + y        
      }

      val es: ExecutorService = Executors.newFixedThreadPool(3)
      assert(f(es).get == 24, "It should add up")

      Par.fork(a)
    }

    it("sum Ints") {
      import fpinscala.parallelism.Examples
      val f: (Int, Int) => Int = {case (a,b) => a + b} 
      val es: ExecutorService = Executors.newFixedThreadPool(7)
      val res: Par[Int] = Examples.sum(Vector[Int](1,2,3,4,5,6,7,8))(f)      
      assert(res(es).get(1, TimeUnit.SECONDS) === 36)
    }
    
    it("test a lazyUnit") {
      val es: ExecutorService = Executors.newFixedThreadPool(1)
      val res = Par.lazyUnit(1)
      assert(res(es).get(1, TimeUnit.SECONDS) === 1)
    }

    it("test asyncF") {
      val a = unit(18)
      val es: ExecutorService = null
      val f: Int => Int = { a =>
        {
          for (i <- 1 to 100) {
            Thread.sleep(12)
            println("ss")
            a + i
          }
          a
        }
      }
      val x = Par.asyncF(f)

      x(2)
    }

    it("test sorted Par List") {
      val l = List(1, 2, 3, 4, 5, 0, -2, -1, -99)
      val parList: Par[List[Int]] = Par.lazyUnit(l)

      val parListSorted = Par.sortPar(parList)
      val es: ExecutorService = Executors.newFixedThreadPool(1)

      assert(parListSorted(es).get(1, TimeUnit.SECONDS) == l.sorted, "It should be sorted in par...")
    }

    it("test ParMap") {
      val parList: List[Int] = List(1, 2, 3, 4, 5, 0, -2, -1, -99)

      val es: ExecutorService = Executors.newFixedThreadPool(20)
      val parMap = Par.parMap(parList) {x => x*2}(es)
           
      eventually {
        assert(parMap.get(1, TimeUnit.SECONDS).size === 9)
      }
    }

    it("ParFilter") {
      val parList: List[Int] = List(1, 2, 3, 4, 5, 0, -2, -1, -99)

      val parMap = Par.parFilter(parList) {a: Int => true}
      val es: ExecutorService = Executors.newFixedThreadPool(20)
      
      assert(parMap(es).get(1, TimeUnit.SECONDS).size === 9)
    }
    
    it("max Ints") {
      import fpinscala.parallelism.Examples
      val f: (Int, Int) => Int = {case (a,b) => a max b} 
      val es: ExecutorService = Executors.newFixedThreadPool(25)
      val res: Par[Int] = Examples.sum(Vector[Int](1,2,3,4,5,6,7,8,89,99,19992,99,82))(f)      
      assert(res(es).get(1, TimeUnit.SECONDS) === 19992)
    }
    
     it("max Words") {
      val es: ExecutorService = Executors.newFixedThreadPool(2)      
      val paras = List("This is paragraph 1", "And this is paragraph 2, which is slightly longer than the other one",
          "Paragraph 3 is pretty short really")
      
      val res: Par[Int] = Examples.maxWords(paras)     
      assert(res(es).get(1, TimeUnit.SECONDS) === 23)
    }
      
    it("tests map3") {
      val es: ExecutorService = Executors.newFixedThreadPool(2)      
      val paras = List("This is paragraph 1", "And this is paragraph 2, which is slightly longer than the other one",
          "Paragraph 3 is pretty short really")
      
      val res: Par[Int] = Examples.maxWords(paras)
      
      val result = map3(res, unit(3), unit(4))((a,b,c) => a + b + c)      
      assert(result(es).get(1, TimeUnit.SECONDS) === 30)
    }     
    
    
    it("7.4.1 mapping") {
      val es: ExecutorService = null
      val p1 = map(unit(1))(_ + 1)
      val p2 = unit(2)
      assert(equal(es)(p1,  p2))
    }
    
    it("7.4.2 fork law") {
      val es: ExecutorService = Executors.newFixedThreadPool(1)
      val p2 = unit(2)
      assert(equal(es)(fork(p2), p2))
    }

    it("7.8 fork bug") {
      val a: Par[Int] = lazyUnit(42 + 1)
      val S: ExecutorService = Executors.newFixedThreadPool(2)
      assert(equal(S)(a, fork(a)))
    }
        
  }

}