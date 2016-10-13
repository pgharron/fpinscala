object hakker {

  /*
  def main(args: Array[Int]) {

    var n = 6;
    var arr = new Array[Int](n);
    for (arr_i <- 0 to n - 1) {
      arr(arr_i) = args(arr_i)
    }

    val g: List[Int] = args.map(k => k).toList

    println(g.mkString(","))

    def rnd(x: Double): BigDecimal = {
      BigDecimal(x).setScale(6, BigDecimal.RoundingMode.HALF_UP)
    }

		val r = n.toDouble
    val bs: List[Tuple3[BigDecimal, BigDecimal, BigDecimal]] = g.foldLeft[(Int, Int, Int)]((0, 0, 0)) {
      (result, v) =>
        val (p, n, z) = result
        if (v > 0) ((p + 1, n, z))
        else if (v < 0) ((p, n + 1, z))
        else ((p, n, z + 1))
    } match {
      case (p, n, z) => List((rnd(p.toDouble / r), rnd(n / r), rnd(z / r)))
    }
    
    println(bs.flatMap{t => List(t._1, t._2, t._3)}.mkString("\n"))
  }

  main(Array(-4, 3, -9, 0, 4, 1))
 */

  def printStairs(n: Int) {
    for (i <- n - 1 to 0 by -1) {
      println((" " * i) + "#" * (n - i))
    }
  }                                               //> printStairs: (n: Int)Unit

  printStairs(6)                                  //>      #
                                                  //|     ##
                                                  //|    ###
                                                  //|   ####
                                                  //|  #####
                                                  //| ######

}