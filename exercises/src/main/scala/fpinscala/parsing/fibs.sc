package fpinscala.parsing

object fibs {

  def fibnaive(n: BigInt): BigInt =
    if (n <= 1)
      n
    else {
      val r = fibnaive(n - 1)
      val s = fibnaive(n - 2)
      r + s
    }                                             //> fibnaive: (n: BigInt)BigInt

  type Memo = Map[BigInt, BigInt]

  def fibmemo1(n: BigInt): BigInt = {
    def fibmemoR(z: BigInt, memo: Memo): (BigInt, Memo) = {
      println(s"doing $z memo=$memo")
      if (z <= 1) { println("and back...") ; (z, memo)}
        
      else memo get z match {
        case None => {
          val (r, memo0) = fibmemoR(z - 1, memo)
          val (s, memo1) = fibmemoR(z - 2, memo0)
          (r + s, memo1 + (z -> (r + s)))
          //(r + s, memo1)
        }
        case Some(v) => {println(s"some $v from $z");        (v, memo)}
      }
		}
    fibmemoR(n, Map())._1
  }                                               //> fibmemo1: (n: BigInt)BigInt
     
  fibmemo1(4)                                     //> doing 4 memo=Map()
                                                  //| doing 3 memo=Map()
                                                  //| doing 2 memo=Map()
                                                  //| doing 1 memo=Map()
                                                  //| and back...
                                                  //| doing 0 memo=Map()
                                                  //| and back...
                                                  //| doing 1 memo=Map(2 -> 1)
                                                  //| and back...
                                                  //| doing 2 memo=Map(2 -> 1, 3 -> 2)
                                                  //| some 1 from 2
                                                  //| res0: BigInt = 3

	case class State[S, A](run: S => (A, S))

  def fibmemo2(n: BigInt): BigInt = {
    def fibmemoR(z: BigInt): State[Memo, BigInt] =
      State(memo =>
        if(z <= 1)
          (z, memo)
        else memo get z match {
          case None => {
            val (r, memo0) = fibmemoR(z - 1) run memo
            val (s, memo1) = fibmemoR(z - 2) run memo
            (r + s, memo1)
          }
          case Some(v) => (v, memo)
        })

    fibmemoR(n).run(Map())._1
  }                                               //> fibmemo2: (n: BigInt)BigInt

	//fibnaive(10)
	

	//fibmemo2(12)

}