package com.github.jostly.scalatest.mock.mockito

import com.github.jostly.scalatest.mock.MockitoSweetener
import org.mockito.exceptions.verification.ArgumentsAreDifferent
import org.scalactic.TripleEqualsSupport.Spread
import org.scalatest.{Matchers, WordSpec}

class MockitoMatchersSpec extends WordSpec with Matchers with MockitoSweetener {

  trait Probe {
    def mInt(i: Int): Int
    def mAny(a: Any): Any
    def mList(l: List[Int]): Unit
    def mDouble(d: Double): Unit
  }

  List(1).contains(2)

  "Equality matchers" should {
    def probe = {
      val probe = mock[Probe]
      probe.mInt(17)
      probe.mAny(Some("Foo"))
      probe
    }
    "match on equality" in {
      there was one(probe).mInt(is(17))
      there was one(probe).mAny(is(Some("Foo")))
    }
    "not match when arguments are not equal" in {
      expectToFail(there was one(probe).mInt(is(16)))
      expectToFail(there was one(probe).mAny("Foo"))
    }
  }

  "Type matchers" when {
    "matching on reference types" should {
      def probe = {
        val probe = mock[Probe]
        probe.mAny("Foo")
        probe
      }
      "match where actual class is the same as argument" in {
        there was one(probe).mAny(any[String])
      }
      "match where actual class is subclass of argument" in {
        there was one(probe).mAny(any[AnyRef])
      }
      "not match when class differs" in {
        expectToFail(there was one(probe).mAny(any[List[Int]]))
      }
    }
    "matching on value types" should {
      def probe = {
        val probe = mock[Probe]
        probe.mAny(17)
        probe
      }
      "match when actual value type is the same as argument" in {
        there was one(probe).mAny(any[Int])
      }
      "not match when value types differ" in {
        expectToFail(there was one(probe).mAny(any[Float]))
      }
    }
  }

  "Wildcard matchers" should {
    def probe = {
      val probe = mock[Probe]
      probe.mInt(17)
      probe.mAny("String")
      probe
    }
    "match anything" in {
      there was one(probe).mInt(*)
      there was one(probe).mAny(*)
    }
  }

  "ScalaTest matchers" should {
    def probe = {
      val probe = mock[Probe]
      probe.mInt(17)
      probe.mAny("String")
      probe.mList(List(1, 2, 5))
      probe.mDouble(3.2)
      probe
    }
    "match if the underlying matcher matches" in {
      there was one(probe).mAny(should(include("tri"))) // Any/AnyRef methods must explicitly convert matchers...
      there was one(probe).mList(contain(2)) // ...in other cases, implicit conversion does the trick
      there was one(probe).mList(contain(2) and have length 3)
      there was one(probe).mInt(be < 20)
      there was one(probe).mDouble(within(3.3 +- 0.15))
    }
    "not match if the underlying matcher does not match" in {
      expectToFail(there was one(probe).mAny(should(include("x"))))
      expectToFail(there was one(probe).mList(contain(3)))
      expectToFail(there was one(probe).mList(contain(2) and have length 4))
      expectToFail(there was one(probe).mInt(be < 17))
    }
  }

  def expectToFail(f: => Unit) = an[ArgumentsAreDifferent] should be thrownBy f
}
