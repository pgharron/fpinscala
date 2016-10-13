import fpinscala.monoids.Monoid._

object Monoids {

  val words = List("Hick", "orory", "dickoory")   //> words  : List[String] = List(Hick, orory, dickoory)

  val s = words.foldRight(stringMonoid.zero)(stringMonoid.op)
                                                  //> s  : String = Hickororydickoory

  val r = words.foldLeft(stringMonoid.zero)(stringMonoid.op)
                                                  //> r  : String = Hickororydickoory

  words.foldLeft("")(_ + _)                       //> res0: String = Hickororydickoory

  concatenate(words, stringMonoid)                //> res1: String = Hickororydickoory

  case class Dog(name: String)

  val ds = List(Dog("Brandy"), Dog("Kirby"), Dog("Lady"))
                                                  //> ds  : List[Monoids.Dog] = List(Dog(Brandy), Dog(Kirby), Dog(Lady))

  foldMap[Dog, String](ds, stringMonoid)(d => d.name)
                                                  //> res2: String = BrandyKirbyLady
  
	foldRight[Dog, String](ds)("")((d, s) => d.name + s)
                                                  //> res3: String = BrandyKirbyLady
              
  foldRight[String, String](words)("")((d, s) => d + s)
                                                  //> res4: String = Hickororydickoory
}