package fpinscala.parallelism

import java.util.concurrent._

object Par {

  type Par[A] = ExecutorService => Future[A]

  def run[A](s: ExecutorService)(a: Par[A]): Future[A] = a(s)

  // `unit` is represented as a function that returns a `UnitFuture`, which is a 
  // simple implementation of `Future` that just wraps a constant value. It doesn't 
  // use the `ExecutorService` at all. It's always done and can't be cancelled. 
  // Its `get` method simply returns the value that we gave it.
  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  //Exercise 7.4
  def asyncF[A, B](f: A => B): A => Par[B] = {
    a => lazyUnit(f(a))
  }

  private case class UnitFuture[A](get: A) extends Future[A] {
    def isDone = true
    def get(timeout: Long, units: TimeUnit) = get
    def isCancelled = false
    def cancel(evenIfRunning: Boolean): Boolean = false
  }

  //  def map2[A,B,C](a: Par[A], b: Par[B])(f: (A,B) => C): Par[C] = // `map2` doesn't evaluate the call to `f` in a separate logical thread, in accord with our design choice of having `fork` be the sole function in the API for controlling parallelism. We can always do `fork(map2(a,b)(f))` if we want the evaluation of `f` to occur in a separate thread.
  //    (es: ExecutorService) => {
  //      val af = a(es) 
  //      val bf = b(es)
  //      UnitFuture(f(af.get, bf.get)) // This implementation of `map2` does _not_ respect timeouts, and eagerly waits for the returned futures. This means that even if you have passed in "forked" arguments, using this map2 on them will make them wait. It simply passes the `ExecutorService` on to both `Par` values, waits for the results of the Futures `af` and `bf`, applies `f` to them, and wraps them in a `UnitFuture`. In order to respect timeouts, we'd need a new `Future` implementation that records the amount of time spent evaluating `af`, then subtracts that time from the available time allocated for evaluating `bf`.
  //    }

  // Exercise 7.3
  /* This version respects timeouts. See `Map2Future` below. */
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
    es => {
      val (af, bf) = (a(es), b(es))
      Map2Future(af, bf, f)
    }

  def map3[A, B, C, D](a: Par[A], b: Par[B], c: Par[C])(f: (A, B, C) => D): Par[D] =
    map2(map2(a, b)((a, b) => (c: C) => f(a, b, c)), c)(_(_))

  def map4[A, B, C, D, E](a: Par[A], b: Par[B], c: Par[C], d: Par[D])(f: (A, B, C, D) => E): Par[E] =
    map2(map3(a, b, c)((a, b, c) => (d: D) => f(a, b, c, d)), d)(_(_))

  def map5[A, B, C, D, E, F](a: Par[A], b: Par[B], c: Par[C], d: Par[D], e: Par[E])(f: (A, B, C, D, E) => F): Par[F] =
    map2(map4(a, b, c, d)((a, b, c, d) => (e: E) => f(a, b, c, d, e)), e)(_(_))

  /*
Note: this implementation will not prevent repeated evaluation if multiple threads call `get` in parallel. We could prevent this using synchronization, but it isn't needed for our purposes here (also, repeated evaluation of pure values won't affect results).
*/
  case class Map2Future[A, B, C](a: Future[A], b: Future[B],
                                 f: (A, B) => C) extends Future[C] {
    @volatile var cache: Option[C] = None
    def isDone = cache.isDefined
    def isCancelled = a.isCancelled || b.isCancelled
    def cancel(evenIfRunning: Boolean) =
      a.cancel(evenIfRunning) || b.cancel(evenIfRunning)
    def get = compute(Long.MaxValue)
    def get(timeout: Long, units: TimeUnit): C =
      compute(TimeUnit.NANOSECONDS.convert(timeout, units))

    private def compute(timeoutInNanos: Long): C = cache match {

      case Some(c) => c
      case None =>
        val start = System.nanoTime
        val ar = a.get(timeoutInNanos, TimeUnit.NANOSECONDS)
        val stop = System.nanoTime; val aTime = stop - start
        val br = b.get(timeoutInNanos - aTime, TimeUnit.NANOSECONDS)
        val ret = f(ar, br)
        cache = Some(ret)
        ret
    }
  }

  // Exercise 7.5
  def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = {
    val fbs: List[Par[B]] = ps.map(asyncF(f))
    sequence(fbs)
  }

  def sequence_simple[A](l: List[Par[A]]): Par[List[A]] =
    l.foldRight[Par[List[A]]](unit(List()))((h, t) => map2(h, t)(_ :: _))

  // This implementation forks the recursive step off to a new logical thread,
  // making it effectively tail-recursive. However, we are constructing
  // a right-nested parallel program, and we can get better performance by 
  // dividing the list in half, and running both halves in parallel. 
  // See `sequenceBalanced` below.
  def sequenceRight[A](as: List[Par[A]]): Par[List[A]] =
    as match {
      case Nil    => unit(Nil)
      case h :: t => map2(h, fork(sequence(t)))(_ :: _)
    }

  // We define `sequenceBalanced` using `IndexedSeq`, which provides an 
  // efficient function for splitting the sequence in half. 
  def sequenceBalanced[A](as: IndexedSeq[Par[A]]): Par[IndexedSeq[A]] = fork {
    if (as.isEmpty) unit(Vector())
    else if (as.length == 1) map(as.head)(a => Vector(a))
    else {
      val (l, r) = as.splitAt(as.length / 2)
      map2(sequenceBalanced(l), sequenceBalanced(r))(_ ++ _)
    }
  }

  def sequence[A](as: List[Par[A]]): Par[List[A]] =
    map(sequenceBalanced(as.toIndexedSeq))(_.toList)

  // Exercise 7.6
  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = {
    val pars: List[Par[List[A]]] = as map (asyncF((a: A) => if (f(a)) List(a) else List()))
    map(sequence(pars))(_.flatten) // convenience method on `List` for concatenating a list of lists    
  }

  def fork[A](a: => Par[A]): Par[A] = // This is the simplest and most natural implementation of `fork`, but there are some problems with it--for one, the outer `Callable` will block waiting for the "inner" task to complete. Since this blocking occupies a thread in our thread pool, or whatever resource backs the `ExecutorService`, this implies that we're losing out on some potential parallelism. Essentially, we're using two threads when one should suffice. This is a symptom of a more serious problem with the implementation, and we will discuss this later in the chapter.
    es => es.submit(new Callable[A] {
      def call = a(es).get
    })

  def map[A, B](pa: Par[A])(f: A => B): Par[B] =
    map2(pa, unit(()))((a, _) => f(a))

  def sortPar(parList: Par[List[Int]]) = map(parList)(_.sorted)

  def equal[A](e: ExecutorService)(p: Par[A], p2: Par[A]): Boolean =
    p(e).get == p2(e).get

  def delay[A](fa: => Par[A]): Par[A] =
    es => fa(es)

  def choice[A](cond: Par[Boolean])(t: Par[A], f: Par[A]): Par[A] =
    es =>
      if (run(es)(cond).get) t(es) // Notice we are blocking on the result of `cond`.
      else f(es)

  // Exercise 7.11
  def choiceN[A](n: Par[Int])(choices: List[Par[A]]): Par[A] =
    es => {
      val i = run(es)(n).get // Notice we are blocking on the result of `cond`.            
      run(es)(choices(i))
    }

  // Exercise 7.12    
  def choiceMap[K, V](n: Par[K])(choices: Map[K, Par[V]]): Par[V] =
    es => {
      val k = run(es)(n).get // Notice we are blocking on the result of `cond`.            
      run(es)(choices(k))
    }

  def choiceViaChoiceN[A](a: Par[Boolean])(ifTrue: Par[A], ifFalse: Par[A]): Par[A] =
    choiceN(map(a)(b => if (b) 0 else 1))(List(ifTrue, ifFalse))

  def choiceViaChooser[A](a: Par[Boolean])(ifTrue: Par[A], ifFalse: Par[A]): Par[A] =
    chooser(a)(b => if (b) ifTrue else ifFalse)

  def choiceNViaChooser[A](n: Par[Int])(choices: List[Par[A]]): Par[A] =
    chooser(n)(i => choices(i))

  // Exercise 7.13
  def chooser[A, B](pa: Par[A])(choices: A => Par[B]): Par[B] =
    es => {
      val a = run(es)(pa).get
      run(es)(choices(a))
    }

  // Exercise 7.14
  def join[A](a: Par[Par[A]]): Par[A] = {
    es => run(es)((run(es)(a).get()))
  }

  def flatMap[A, B](p: Par[A])(f: A => Par[B]): Par[B] =
    join(map(p)(f))

  def joinViaFlatMap[A](a: Par[Par[A]]): Par[A] =
    flatMap(a)(x => x)

  def flatMapViaJoin[A, B](p: Par[A])(f: A => Par[B]): Par[B] =
    join(map(p)(f))

  /* Gives us infix syntax for `Par`. */
  implicit def toParOps[A](p: Par[A]): ParOps[A] = new ParOps(p)

  class ParOps[A](p: Par[A]) {
  }
}

object Examples {
  import Par._

  def maxWords(pgs: List[String]): Par[Int] = {

    def countWords(text: String): Int = {
      val counts = scala.collection.mutable.Map.empty[String, Int].withDefaultValue(0)
      for (rawWord <- text.split("[ ,!.]+")) {
        val word = rawWord.toLowerCase
        counts(word) += 1
      }

      counts.values.sum
    }

    if (pgs.size <= 1) {
      val p = pgs.headOption
      Par.unit(if (p.isDefined) countWords(p.get) else 0)
    } else {
      val (l, r) = pgs.splitAt(pgs.length / 2)
      Par.map2(Par.fork(maxWords(l)), Par.fork(maxWords(r)))(_ + _)
    }
  }

 
//  def maxWords(pgs: List[String]): Par[Int] = {
//    
//    def countWords(text: String):Int = {
//      val counts = scala.collection.mutable.Map.empty[String, Int].withDefaultValue(0)
//        for (rawWord <- text.split("[ ,!.]+")) {
//           val word = rawWord.toLowerCase
//              counts(word) += 1
//      }
//      
//     counts.values.sum
//    }
//        
//    if (pgs.size <= 1) {     
//      val p = pgs.headOption
//      Par.unit(if (p.isDefined) countWords(p.get) else 0)
//    } else {
//      val (l, r) = pgs.splitAt(pgs.length / 2) 
//      Par.map2(Par.fork(maxWords(l)), Par.fork(maxWords(r))) (_ + _)
//    }      
//  }
//  
//  
  def sum(ints: IndexedSeq[Int])(f:(Int, Int) => Int): Par[Int] = // `IndexedSeq` is a superclass of random-access sequences like `Vector` in the standard library. Unlike lists, these sequences provide an efficient `splitAt` method for dividing them into two parts at a particular index.
    if (ints.size <= 1)      
      Par.unit(ints.headOption getOrElse 0) // `headOption` is a method defined on all collections in Scala. We saw this function in chapter 3.
    else {
      val (l, r) = ints.splitAt(ints.length / 2) // Divide the sequence in half using the `splitAt` function.
      Par.map2(Par.fork(sum(l)(f)), Par.fork(sum(r)(f))) { (a, b) => f(a, b) }
      //      (_ + _) // Recursively sum both halves and add the results together.
    }

  
  
}
