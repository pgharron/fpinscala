package fpinscala.parsing

import fpinscala.parsing._
import fpinscala.parsing.Parsers._

import org.scalatest.FunSpec
import java.util.concurrent._
import org.scalatest.concurrent.Eventually._

class ParsingSpec extends FunSpec {

  describe("The Parsing mechanism") {

//    class MyParser[+A](s: String)
//
//    object MyParser extends Parsers[String, MyParser] {
//      def apply(s: String) = new MyParser(s)
//    }
//  
//    it("test Char") {
//      import MyParser.{ run => r }
//      import MyParser._
//
//      val a = "a"
//
//      assert(r(char('a'))(a.toString) === 2)
//    }


    
    it("string") {
      val s = "anyString"
      //      assert(run(string(s))(s) === Right(s))
    }

    it("or") {
      val a = "abra"
      val b = "cadabra"
      //      assert(run(or(string(a), string(b)))(a) === Right(a))
      //      assert(run(or(string(a), string(b)))(b) === Right(a))
    }

    it("listOfN") {
      //      run(listOfN(3, "ab" | "cad"))("ababcad") == Right("ababcad")
      //      run(listOfN(3, "ab" | "cad"))("cadabab") == Right("cadabab")
      //      run(listOfN(3, "ab" | "cad"))("ababab") == Right("ababab")

    }

    it("Int/many") {
      //      val numA: Parser[Int] = char('a').many.map(_.size)
      
      //using slice p. 154
      //val numASlice: Parser[Int] = char('a').many.slice.map(_.size)
      
      //      run(numA)("aaa") == Right(3)
      //      run(numA)("a") == Right(0)      
    }

    it("succeed") {
      // run(succeed(a)("aaa") == Right(a)      
    }    

    it("slice") {
      
      
      
      // run(slice(('a'|'b').many))("aaba") === Right("aaba")      
    }       

    it("Ex. 9.6") {
   
    }   
    
  }
}