package fpinscala.parsing

import fpinscala.parsing._
import fpinscala.parsing.Parsers._


import org.scalatest.FunSpec
import java.util.concurrent._
import org.scalatest.concurrent.Eventually._


class JSONSpec extends FunSpec {

val jsonTxt = """
{
  "Company name" : "Microsoft Corporation",
  "Ticker"  : "MSFT",
  "Active"  : true,
  "Price"   : 30.66,
  "Shares outstanding" : 8.38e9,
  "Related companies" : [ "HPQ", "IBM", "YHOO", "DELL", "GOOG" ]
}
"""
  
  describe("The JSON Parser mechanism") {

//    it("should parse a JSON string") {
//      import fpinscala.parsing.ReferenceTypes.Parser
//      val P = fpinscala.parsing.Reference
//      val json: Parser[JSON] = JSON.jsonParser(P)
//      
//     assert(P.run(json)(jsonTxt) === Right("AA"))
//    }

  }
}