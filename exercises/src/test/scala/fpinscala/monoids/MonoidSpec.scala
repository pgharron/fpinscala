

package fpinscala.monoids

import Monoid._
import org.scalatest.FunSpec
import fpinscala.testing._

class MonoidSpec extends FunSpec {

    it("boolean and") {
      Prop.run(monoidLaws[Boolean](booleanAnd, Gen.boolean)) 
    }

    it("boolean or") {
      Prop.run(monoidLaws[Boolean](booleanOr, Gen.boolean)) 
    }
    
    
}