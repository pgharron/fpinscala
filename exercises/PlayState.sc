object PlayState {
  import fpinscala.state._
  import fpinscala.state.RNG._
  
  
  val s = Simple(5)                               //> s  : fpinscala.state.RNG.Simple = Simple(5)
  
 
doubleUsingMap(s)                                 //> res0: (Double, fpinscala.state.RNG) = (8.958131074905396E-4,Simple(126074519
                                                  //| 596))
                                                  
doubleUsingMap                                    //> res1: fpinscala.state.RNG.Rand[Double] = <function1>

val x = int                                       //> x  : fpinscala.state.RNG.Rand[Int] = <function1>

x(s)                                              //> res2: (Int, fpinscala.state.RNG) = (1923744,Simple(126074519596))

	map(int){a => a+2}{
		s
	}                                         //> res3: (Int, fpinscala.state.RNG) = (1923746,Simple(126074519596))
	
	
	
}