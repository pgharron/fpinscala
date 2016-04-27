package fpinscala.parsing
import fpinscala.testing._
import scala.util.matching.Regex

trait Parsers[ParseError, Parser[+_]] { self => // so inner classes may call methods of trait

  def char(c: Char): Parser[Char] =
    string(c.toString).map(_.charAt(0))

  // always succeeds with the value a
  def succeed[A](a: A): Parser[A] =
    string("").map(_ => a)

  // Chooses between two parsers, first attempting p1, and then p2 if p1 fails  
  def or[A](s1: Parser[A], s2: => Parser[A]): Parser[A]

  // Recognises and returns a single String
  implicit def string(s: String): Parser[String]

  implicit def operators[A](p: Parser[A]) = ParserOps[A](p)

  implicit def asStringParser[A](a: A)(implicit f: A => Parser[String]): ParserOps[String] = ParserOps(f(a))

  implicit def regex(r: Regex): Parser[String]

  def listOfN[A](n: Int, p: Parser[A]): Parser[List[A]] = // what is in list e.g. List[Parser[Char]]??
    if (n <= 0) succeed(List())
    else map2(p, listOfN(n - 1, p))(_ :: _)

  // Ex. 9.2  
  def many[A](p: Parser[A]): Parser[List[A]] = {
    map2(p, many(p))(_ :: _) or succeed(List())
  }

  // applies the function f to the result of p, if successful
  def map[A, B](p: Parser[A])(f: A => B): Parser[B] = {
    flatMap(p)(b => succeed(f(b)))
  }

  // Returns the portion of input inspected by p if successful
  def slice[A](p: Parser[A]): Parser[String]

  // Sequences two parsers, running p1 and then p2, and then p2, and returns the  
  // pair of their results if both succeed
  def product[A, B](p: Parser[A], p2: => Parser[B]): Parser[(A, B)] = {
    flatMap(p)(a => map(p2)(b => (a, b)))
  }

  // Ex. 9.1
  def map2[A, B, C](p: Parser[A], p2: => Parser[B])(f: (A, B) => C): Parser[C] =
    for { a <- p; b <- p2 } yield f(a,b) // Cool!

  def many1[A](p: Parser[A]): Parser[List[A]] =
    map2(p, many(p))(_ :: _)

  def flatMap[A, B](p: Parser[A])(f: A => Parser[B]): Parser[B]

  // Ex. 9.6  
  def dfo()= {
    for {
      digit <- "[0-9]+".r
      val n = digit.toInt // we really should catch exceptions thrown by toInt and convert to parse failure
      _ <- listOfN(n, char('a'))
    } yield n
  }

  object Laws {

    def equal[A](p1: Parser[A], p2: Parser[A])(in: Gen[String]): Prop =
      fpinscala.testing.Prop.forAll(in)(s => run(p1)(s) == run(p2)(s))

    def mapLaw[A](p: Parser[A])(in: Gen[String]): Prop =
      equal(p, p.map(a => a))(in)

  }

  def run[A](p: Parser[A])(input: String): Either[ParseError, A]

  case class ParserOps[A](p: Parser[A]) {
    def |[B >: A](p2: Parser[B]): Parser[B] = self.or(p, p2)
    def or[B >: A](p2: => Parser[B]): Parser[B] = self.or(p, p2)
    def **[B >: A](p2: => Parser[B]): Parser[(A, B)] = self.product(p, p2)
    def product[B >: A](p2: => Parser[B]): Parser[(A, B)] = self.product(p, p2)

    def map[B](f: A => B): Parser[B] = self.map(p)(f)
    def many = self.many(p)
    def slice = self.slice(p)
    def flatMap[B](f: A => Parser[B]): Parser[B] = self.flatMap(p)(f)
  }

}

case class Location(input: String, offset: Int = 0) {

  lazy val line = input.slice(0, offset + 1).count(_ == '\n') + 1
  lazy val col = input.slice(0, offset + 1).reverse.indexOf('\n')

  def toError(msg: String): ParseError =
    ParseError(List((this, msg)))

  def advanceBy(n: Int) = copy(offset = offset + n)

  /* Returns the line corresponding to this location */
  def currentLine: String =
    if (input.length > 1) input.lines.drop(line - 1).next
    else ""
}

case class ParseError(stack: List[(Location, String)] = List(),
                      otherFailures: List[ParseError] = List()) {
}

object Parsers {

}

