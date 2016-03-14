package fpinscala.testing

import org.scalatest.FunSpec
import fpinscala.state._
import fpinscala.state.RNG._
import fpinscala.parallelism.Par
import fpinscala.parallelism.Par._

class GenSpec extends FunSpec {

  describe("Gen") {

    ignore("unit") {
      val u = Gen.unit(3).sample.run(Simple(10))._1
      assert(u === 3)
    }

    ignore("boolean") {
      val u = Gen.boolean.sample.run(Simple(2))._1
      assert(u === false)
    }

    ignore("should choose") {

      val xx = Simple(10)
      val c: Gen[Int] = Gen.choose(1, 10)
      val g = c.sample.run(xx)

      val intList = Gen.listOfN(22, c)

      intList.sample.run(xx)

      assert(intList.sample.run(xx) === 1)
    }

    ignore("should choose 1") {

      val rng = Simple(110)
      val c: Gen[Int] = Gen.choose(0, 4)
      val intList = c.listOfN(c).sample.run(rng)

      println(s"k =========== $intList")
    }

    ignore("test union") {

      val rng = Simple(11)
      val g1: Gen[Int] = Gen.choose(0, 100)
      val g2: Gen[Int] = Gen.choose(3, 55)

      val res = Gen.union(g1, g2).sample

      println(s"res = ${res.run(rng)._1}")
    }

    it("testing 8.4.1") {
      import Prop._
      val smallInt = Gen.choose(-1, 1)
      val maxProp = forAll(Gen.listOf1(smallInt)) { ns =>
        val max = ns.max
        !ns.exists(_ > max)
      }

      Prop.run(maxProp)
    }

    it("testing 8.14") {
      import Prop._
      import Gen._
      val smallInt = Gen.choose(-10, 10)
      val sortedProp = forAll(listOf(smallInt)) { ns =>
        val nss = ns.sorted
        // We specify that every sorted list is either empty, has one element,
        // or has no two consecutive elements `(a,b)` such that `a` is greater than `b`.
        (nss.isEmpty || nss.tail.isEmpty || !nss.zip(nss.tail).exists {
          case (a, b) => a > b
        })
        // Also, the sorted list should have all the elements of the input list,
        !ns.exists(!nss.contains(_))
        // and it should have no elements not in the input list.
        !nss.exists(!ns.contains(_))
      }

      Prop.run(sortedProp)
    }

    it("testing Par") {
      import Prop._
      import java.util.concurrent._
      val ES: ExecutorService = Executors.newCachedThreadPool

      val p3 = check {
        Prop.equal(
          Par.map(Par.unit(1))(_ + 1),
          Par.unit(2))(ES).get
      }
      Prop.run(p3)
    }

    it("testing Pg 141") {
      import Prop._

      val p1 = checkPar {
        equal(
          Par.map(Par.unit(1))(_ + 1),
          Par.unit(2))
      }

      Prop.run(p1)
    }

    it("Exercise 8.16 / 8.17") {
      import Gen._
      import Prop._
      //Exercise 8.16
      val pint2: Gen[Par[Int]] = choose(-100, 100).listOfN(choose(0, 20)).map(l =>
        l.foldLeft(Par.unit(0))((p, i) =>
          Par.fork { Par.map2(p, Par.unit(i))(_ + _) }))

      // Fork Property
      val forkProp = Prop.forAllPar(pint2)(i => equal(Par.fork(i), i)) tag "fork"
      Prop.run(forkProp, 1, 1)
    }
  }
}