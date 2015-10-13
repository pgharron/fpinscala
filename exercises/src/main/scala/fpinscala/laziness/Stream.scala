package fpinscala.laziness

import Stream._
trait Stream[+A] {

  def foldRight[B](z: => B)(f: (A, => B) => B): B = // The arrow `=>` in front of the argument type `B` means that the function `f` takes its second argument by name and may choose not to evaluate it.
    this match {
      case Cons(h, t) =>
        f(h(), t().foldRight(z)(f)) // If `f` doesn't evaluate its second argument, the recursion never occurs.
      case _ => z
    }

  def exists(p: A => Boolean): Boolean =
    foldRight(false)((a, b) => p(a) || b) // Here `b` is the unevaluated recursive step that folds the tail of the stream. If `p(a)` returns `true`, `b` will never be evaluated and the computation terminates early.

  @annotation.tailrec
  final def find(f: A => Boolean): Option[A] = this match {
    case Empty => None
    case Cons(h, t) => if (f(h())) Some(h()) else t().find(f)
  }

  //  def check {
  //    this match {
  //      case Cons(h, t) => println("h,t")
  //      case _ => println("zz")
  //    }
  //  }

  //Exercise 5.2
  /*
	`take` first checks if n==0. In that case we need not look at the stream at all.
   */
  def take(n: Int): Stream[A] = this match {
    case Cons(h, t) if n > 1 => cons(h(), t().take(n - 1))
    case Cons(h, _) if n == 1 => cons(h(), empty)
    case _ => empty
  }

  //Exercise 5.2
  @annotation.tailrec
  final def drop(n: Int): Stream[A] = this match {
    case Cons(_, t) if n > 0 => t().drop(n - 1)
    case _ => this
  }

  //Exercise 5.3
  def takeWhile(f: A => Boolean): Stream[A] = this match {
    case Cons(h, t) if f(h()) => cons(h(), t() takeWhile f)
    case _ => empty
  }

  //Exercise 5.4
  def forAll(f: A => Boolean): Boolean =
    foldRight(true)((a, b) => f(a) && b)

  //Exercise 5.5
  def takeWhile_1(f: A => Boolean): Stream[A] = {
    foldRight(empty[A])((h, t) => if (f(h)) cons(h, t) else empty)
  }

  //Exercise 5.6  
  def headOption: Option[A] = {
    foldRight(None: Option[A])((h, _) => Some(h))
  }

  //Exercise 5.1
  // The natural recursive solution
  def toListRecursive: List[A] = this match {
    case Cons(h, t) => h() :: t().toListRecursive
    case _ => List()
  }

  /*
	The above solution will stack overflow for large streams, since it's
	not tail-recursive. Here is a tail-recursive implementation. At each
	step we cons onto the front of the `acc` list, which will result in the
	reverse of the stream. Then at the end we reverse the result to get the
	correct order again.
	[:ben] are the line breaks above okay? I'm unclear on whether these "hints" are supposed to go in the book or not
	*/
  def toList: List[A] = {
    @annotation.tailrec
    def go(s: Stream[A], acc: List[A]): List[A] = s match {
      case Cons(h, t) => go(t(), h() :: acc)
      case _ => acc
    }
    go(this, List()).reverse
  }

  /* 
In order to avoid the `reverse` at the end, we could write it using a
mutable list buffer and an explicit loop instead. Note that the mutable
list buffer never escapes our `toList` method, so this function is
still _pure_.
*/
  def toListFast: List[A] = {
    val buf = new collection.mutable.ListBuffer[A]
    @annotation.tailrec
    def go(s: Stream[A]): List[A] = s match {
      case Cons(h, t) =>
        buf += h()
        go(t())
      case _ => buf.toList
    }
    go(this)
  }

  // 5.7 map, filter, append, flatmap using foldRight. Part of the exercise is
  // writing your own function signatures.
  def map[B](f: A => B): Stream[B] = {
    foldRight(empty[B])((h, t) => cons(f(h), t))
  }

  def filter(f: A => Boolean): Stream[A] = {
    foldRight(empty[A])((h, t) => if (f(h)) cons(h, t) else t)
  }

  def append[B >: A](s: => Stream[B]): Stream[B] = {
    foldRight(s)((h, t) => cons(h, t))
  }

  def flatMap[B](f: A => Stream[B]): Stream[B] = {
    foldRight(empty[B])((h, t) => f(h) append t)
  }

  // Exercise 5.13
  def mapViaUnfold[B](f: A => B): Stream[B] =
    unfold(this) {
      case Cons(h, t) => Some((f(h()), t()))
      case _ => None
    }

  def takeViaUnfold(n: Int): Stream[A] =
    unfold((this, n)) {
      case (Cons(h, t), 1) => Some((h(), (empty, 0)))
      case (Cons(h, t), n) if n > 1 => Some((h(), (t(), n - 1)))
      case _ => None
    }

  def takeWhileViaUnfold(f: A => Boolean): Stream[A] =
    unfold(this) {
      case Cons(h, t) if (f(h())) => Some((h(), t()))
      case _ => None
    }

  def zipWith[B, C](bs: Stream[B])(f: (A, B) => C): Stream[C] =
    unfold((this, bs)) {
      case (Cons(h, t), Cons(bh, bt)) => Some((f(h(), bh()), (t(), bt())))
      case _ => None
    }

  def zipAll[B](s2: Stream[B]): Stream[(Option[A], Option[B])] =
    unfold((this, s2)) {
      case (Cons(h, t), Cons(bh, bt)) => Some((Some(h()), Some(bh())), (t(), bt()))
      case _ => None
    }

  // THE BOOKS ANSWER IS ===>
  //  special case of `zip`
  //
  //def zip[B](s2: Stream[B]): Stream[(A,B)] =
  //  zipWith(s2)((_,_))
  //
  //
  //def zipAll[B](s2: Stream[B]): Stream[(Option[A],Option[B])] =
  //  zipWithAll(s2)((_,_))
  //
  //def zipWithAll[B, C](s2: Stream[B])(f: (Option[A], Option[B]) => C): Stream[C] =
  //  Stream.unfold((this, s2)) {
  //    case (Empty, Empty) => None
  //    case (Cons(h, t), Empty) => Some(f(Some(h()), Option.empty[B]) -> (t(), empty[B]))
  //    case (Empty, Cons(h, t)) => Some(f(Option.empty[A], Some(h())) -> (empty[A] -> t()))
  //    case (Cons(h1, t1), Cons(h2, t2)) => Some(f(Some(h1()), Some(h2())) -> (t1() -> t2()))
  //  }

  // Exercise 5.14
  def startsWith[B](s: Stream[B]): Boolean = {
    zipAll(s) takeWhile (!_._2.isEmpty) forAll {
      case (h, h2) => h == h2
    }
  }

  // Exercise 5.15
  def tailsPh: Stream[Stream[A]] = {
    unfold(this) {
      case Empty => None
      case a @ Cons(h, t) => Some(a, t())
    } append Stream(empty)
  }

  /*
The last element of `tails` is always the empty `Stream`, so we handle this as a special case, by appending it to the output.
*/
  def tails: Stream[Stream[A]] =
    unfold(this) {
      case Empty => None
      case s => Some((s, s drop 1))
    } append Stream(empty)

  def hasSubSequence[A](s: Stream[A]): Boolean = tails exists (_ startsWith s)

  //Exercise 5.16  
  /*
The function can't be implemented using `unfold`, since `unfold` generates elements of the `Stream` from left to right. It can be implemented using `foldRight` though.
The implementation is just a `foldRight` that keeps the accumulated value and the stream of intermediate results, which we `cons` onto during each iteration. When writing folds, it's common to have more state in the fold than is needed to compute the result. Here, we simply extract the accumulated list once finished.
*/
  def scanRight[B](z: B)(f: (A, => B) => B): Stream[B] =
    foldRight((z, Stream(z)))((a, p0) => {
      // p0 is passed by-name and used in by-name args in f and cons. So use lazy val to ensure only one evaluation...
      lazy val p1 = p0
      println(s"${p0._2.toList}")
      val b2 = f(a, p1._1)

      println(s"b2=$b2 p1=${p1._1},${p1._2.headOption} ")
      (b2, cons(b2, p1._2))
    })._2

}
case object Empty extends Stream[Nothing]
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A]

object Stream {
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  def empty[A]: Stream[A] = Empty

  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty
    else cons(as.head, apply(as.tail: _*))

  val ones: Stream[Int] = Stream.cons(1, ones)

  //Exercise 5.8
  // This is more efficient than `cons(a, constant(a))` since it's just
  // one object referencing itself.
  def constant[A](a: A): Stream[A] = {
    lazy val tail: Stream[A] = Cons(() => a, () => tail)
    tail
  }

  //Exercise 5.9  
  def from(n: Int): Stream[Int] = cons(n, from(n + 1))

  //Exercise 5.10  
  def fibs(): Stream[Int] = {
    def go(f0: Int, f1: Int): Stream[Int] =
      cons(f0, go(f1, f0 + f1))
    go(0, 1)
  }

  //Exercise 5.11  
  def unfold[A, S](z: S)(f: S => Option[(A, S)]): Stream[A] =
    f(z) match {
      case Some((h, s)) => cons(h, unfold(s)(f))
      case None => empty
    }

  // Exercise 5.12
  def fibsUsingUnfold: Stream[Int] =
    unfold((0, 1)) { case (a, b) => Some((a, (b, a + b))) }

  def fromUsingUnfold(a: Int): Stream[Int] =
    unfold(a)(aa => Some((aa, aa + 1)))

  def constantUsingUnfold[A](a: A): Stream[A] =
    unfold(a)(aa => Some((aa, aa)))

  def onesUsingUnfold: Stream[Int] =
    unfold(1)(aa => Some((aa, aa)))

}