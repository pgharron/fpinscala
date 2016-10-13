package fpinscala 
package parsing

import ReferenceTypes._
import scala.util.matching.Regex

object ReferenceTypes {

  /** A parser is a kind of state action that can fail. */
  type Parser[+A] = ParseState => Result[A]

  case class ParseState(loc: Location)
  
  sealed trait Result[+A] {
    def extract: Either[String, A] = this match {
      case Failure(e,_) => Left(e)
      case Success(a,_) => Right(a)
    }
//    /* Used by `attempt`. */
//    def uncommit: Result[A] = this match {
//      case Failure(e,true) => Failure(e,false)
//      case _ => this
//    }
//    /* Used by `flatMap` */
//    def addCommit(isCommitted: Boolean): Result[A] = this match {
//      case Failure(e,c) => Failure(e, c || isCommitted)
//      case _ => this
//    }
//    /* Used by `scope`, `label`. */
//    def mapError(f: ParseError => ParseError): Result[A] = this match {
//      case Failure(e,c) => Failure(f(e),c)
//      case _ => this
//    }
//    def advanceSuccess(n: Int): Result[A] = this match {
//      case Success(a,m) => Success(a,n+m)
//      case _ => this
//    }
  }
  case class Success[+A](get: A, length: Int) extends Result[A]
  case class Failure(get: String, isCommitted: Boolean) extends Result[Nothing]
}

//object Reference extends Parsers[Parser] { // TODO (changed ParserError to String)
//  
//  def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B] = {
//    ???
//  }
//
//  def or[A](s1: ReferenceTypes.Parser[A], s2: => Parser[A]): Parser[A] = {
//    ???
//  }
//
//  def regex(r: Regex): Parser[String] = {
//    ???
//  }
//
//  def run[A](p: Parser[A])(s: String): Either[String, A] = {
//    val s0 = ParseState(Location(s))
//    p(s0).extract
//  }
//
//  def slice[A](p: Parser[A]): Parser[String] = {
//    ???
//  }
//
//  def string(s: String): Parser[String] = {
//    ???
//  }
  
//}