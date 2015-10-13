object GettingStarted {

  // Exercise 1: Write a function to compute the nth fibonacci number
  def fib(n: Int): Int = {

    @annotation.tailrec
    def loop(n: Int, prev: Int, cur: Int): Int = {
      println(s"n=$n, prev=$prev, cur=$cur")
      if (n == 0) prev
      else loop(n - 1, cur, prev + cur)
    }

    loop(n, 0, 1)
  }                                               //> fib: (n: Int)Int

  //fib(3)

  // Exercise 2: Implement a polymorphic function to check whether
  // an `Array[A]` is sorted
  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean = {

    @annotation.tailrec
    def loop(n: Int): Boolean = {
      if (n + 1 >= as.length) true
      else if (gt(as(n), as(n + 1))) false
      else loop(n + 1)
    }

    loop(0)

  }                                               //> isSorted: [A](as: Array[A], gt: (A, A) => Boolean)Boolean
  val b: (Int, Int) => Boolean = { _ >= _ }       //> b  : (Int, Int) => Boolean = <function2>
  // isSorted(Array(1,0), (x:Int, y:Int) => x >= y)

  // Exercise 3: Implement `curry`.
  def curry[A, B, C](f: (A, B) => C): A => (B => C) = a => b => f(a, b)
                                                  //> curry: [A, B, C](f: (A, B) => C)A => (B => C)

  // Exercise 4: Implement `uncurry`
  def uncurry[A, B, C](f: A => B => C): (A, B) => C = (a, b) => f(a)(b)
                                                  //> uncurry: [A, B, C](f: A => (B => C))(A, B) => C

  // Exercise 5: Implement `compose`
  def compose[A, B, C](f: B => C, g: A => B): A => C = (a:A) => f(g(a))
                                                  //> compose: [A, B, C](f: B => C, g: A => B)A => C
}