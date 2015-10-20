object StateCH6 {
  import fpinscala.state._
  import fpinscala.state.RNG._;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(97); 

  val xx = Simple(2);System.out.println("""xx  : fpinscala.state.RNG.Simple = """ + $show(xx ));$skip(16); val res$0 = 
  
  xx.nextInt;System.out.println("""res0: (Int, fpinscala.state.RNG) = """ + $show(res$0));$skip(21); val res$1 = 
  nonNegativeInt(xx);System.out.println("""res1: (Int, fpinscala.state.RNG) = """ + $show(res$1));$skip(16); val res$2 = 
  
  double(xx);System.out.println("""res2: (Double, fpinscala.state.RNG) = """ + $show(res$2));$skip(21); val res$3 = 
  doubleUsingMap(xx);System.out.println("""res3: (Double, fpinscala.state.RNG) = """ + $show(res$3));$skip(16); val res$4 = 
  intDouble(xx);System.out.println("""res4: ((Int, Double), fpinscala.state.RNG) = """ + $show(res$4));$skip(32); val res$5 = 
        
        
  ints(3)(xx);System.out.println("""res5: (List[Int], fpinscala.state.RNG) = """ + $show(res$5));$skip(28); 
  
  val k = unit(4) { xx };System.out.println("""k  : (Int, fpinscala.state.RNG) = """ + $show(k ));$skip(25); val res$6 = 
  nonNegativeEven { xx };System.out.println("""res6: (Int, fpinscala.state.RNG) = """ + $show(res$6));$skip(23); val res$7 = 
  randIntDouble { xx };System.out.println("""res7: ((Int, Double), fpinscala.state.RNG) = """ + $show(res$7));$skip(51); val res$8 = 
  sequence(List(unit(1), unit(2), unit(3)))(xx)._1;System.out.println("""res8: List[Int] = """ + $show(res$8));$skip(17); val res$9 = 
  unit(4) { xx };System.out.println("""res9: (Int, fpinscala.state.RNG) = """ + $show(res$9));$skip(24); val res$10 = 
  rollDie(Simple(5))._1;System.out.println("""res10: Int = """ + $show(res$10));$skip(34); 
  val f1 = nonNegativeLessThan(6);System.out.println("""f1  : fpinscala.state.RNG.Rand[Int] = """ + $show(f1 ));$skip(18); val res$11 = 

f1(Simple(5))._1;System.out.println("""res11: Int = """ + $show(res$11));$skip(42); val res$12 = 


  
  map2(int, int)((a,b) => a * b)(xx);System.out.println("""res12: (Int, fpinscala.state.RNG) = """ + $show(res$12));$skip(89); 
  
  
  val c = List(nonNegativeEven, nonNegativeEven, nonNegativeEven, nonNegativeEven);System.out.println("""c  : List[fpinscala.state.RNG.Rand[Int]] = """ + $show(c ));$skip(18); val res$13 = 
  sequence(c){xx};System.out.println("""res13: (List[Int], fpinscala.state.RNG) = """ + $show(res$13))}
}
