package fpinscala.parallelism

import fpinscala.parallelism.Par._
import org.scalatest.FunSpec
import java.util.concurrent._
import org.scalatest.concurrent.Eventually._

class ParSpec extends FunSpec {

  describe("The Par mechanism") {

    it("should create a Future given a timeout") {

      val a = unit(18)
      val b = unit(6)

      def aPlusb[I](a: Int, b: Int): Int = {
        a + b
      }

      val f: Par[Int] = Par.map2[Int, Int, Int](a, b)(aPlusb(_, _))

      val es: ExecutorService = null
      assert(f(es).get == 24, "It should add up")

      Par.fork(a)
    }

    it("test a lazyUnit") {
      val a = unit(18)
      val es: ExecutorService = null
      val x = Par.lazyUnit(1)
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
      val parList: Par[List[Int]] = Par.unit(l)

      val parListSorted = Par.sortPar(parList)
      val es: ExecutorService = null

      assert(parListSorted(es).get == l.sorted, "It should be sorted in par...")
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
      val es: ExecutorService = Executors.newFixedThreadPool(13)
      
      assert(parMap(es).get(1, TimeUnit.SECONDS).size === 9)
    }

  }

}