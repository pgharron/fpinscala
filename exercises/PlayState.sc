object PlayState {
  import fpinscala.state._
  import fpinscala.state.RNG._
  import fpinscala.state.State._

  val s = Simple(5)                               //> s  : fpinscala.state.RNG.Simple = Simple(5)

  doubleUsingMap(s)                               //> res0: (Double, fpinscala.state.RNG) = (8.958131074905396E-4,Simple(126074519
                                                  //| 596))

  doubleUsingMap                                  //> res1: fpinscala.state.RNG.Rand[Double] = <function1>

  val x = int                                     //> x  : fpinscala.state.RNG.Rand[Int] = <function1>

  x(s)                                            //> res2: (Int, fpinscala.state.RNG) = (1923744,Simple(126074519596))

  map(int) { a => a + 2 } {
    s
  }                                               //> res3: (Int, fpinscala.state.RNG) = (1923746,Simple(126074519596))

  val inputs = List(
        Coin // + 1 coin
        ) // - 1 candy)                           //> inputs  : List[fpinscala.state.Coin.type] = List(Coin)
	
	val y = State.sequence(inputs map (modify[Machine] _ compose VendingMachine.update))
                                                  //> y  : fpinscala.state.State[fpinscala.state.Machine,List[Unit]] = State(<func
                                                  //| tion1>)
  y.run(new Machine(true, 2, 2))._2               //> res4: fpinscala.state.Machine = Machine(false,2,3)


}