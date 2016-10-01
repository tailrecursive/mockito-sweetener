package com.github.jostly.scalatest.mock.mockito

import com.github.jostly.scalatest.mock.MockitoSweetener
import org.scalatest.exceptions.TestFailedException
import org.scalatest.{Matchers, WordSpec}

class CapturingSpec extends WordSpec with Matchers with MockitoSweetener with Capturing {

  "using `captured.as`" should {
    "capture the arguments under the specified label" in {
      val probe = mock[Probe]
      probe.single("foo")

      capturing {
        there was one(probe).single(captured as "a")
        captured.get[String]("a") shouldBe "foo"
      }
    }
    "capture from all invocations" in {
      val probe = mock[Probe]
      probe.single("foo")
      probe.single("bar")

      capturing {
        there were two(probe).single(captured as "a")

        captured.get[String]("a") shouldBe "bar"
        captured.getAll[String]("a") shouldBe Seq("foo", "bar")
      }
    }
    "throw useful exception if no such capture available" in {
      val probe = mock[Probe]
      probe.single("foo")

      capturing {
        there was one(probe).single(captured as "a")

        a[NoSuchElementException] shouldBe thrownBy (captured.get("b"))
      }
    }
    "throw useful exception if getting wrong type from capture" in {
      val probe = mock[Probe]
      probe.single("foo")

      capturing {
        there was one(probe).single(captured as "a")

        a[ClassCastException] shouldBe thrownBy (captured.get[Int]("a"))
      }
    }
    "be able to capture several arguments at the same call" in {
      val probe = mock[Probe]
      probe.multi(17, "seventeen")
      probe.multi(23, List(23))

      capturing {
        there were two(probe).multi(captured as "int", captured as "anyref")

        captured.getAll[Int]("int") shouldBe Seq(17, 23)
        captured.getAll[AnyRef]("anyref") shouldBe Seq("seventeen", List(23))
      }
    }
  }

  "using `verified.by`" should {
    "throw a test failed if the verification function is not defined at value" in {
      val probe = mock[Probe]
      probe.single("foo")

      a[TestFailedException] shouldBe thrownBy (capturing {
        there was one(probe).single(verified[String] by { case "bar" => })
      })
    }
    "pass if the verification function is defined at value and does not throw" in {
      val probe = mock[Probe]
      probe.single("foo")

      capturing {
        there was one(probe).single(verified[String] by { case s => })
      }
    }
    "call supplied verifications" in {
      val probe = mock[Probe]
      probe.single("foo")

      a[TestFailedException] shouldBe thrownBy (capturing {
        there was one(probe).single(verified[String] by {
          case s =>
            s shouldBe "bar"
        })
      })
    }
    "perform verifications on all calls" in {
      val probe = mock[Probe]
      probe.single("foo")
      probe.single("bar")
      var verifiedValues = Vector.empty[String]

      capturing {
        there were two(probe).single(verified[String] by { case s => verifiedValues :+= s })
      }

      verifiedValues shouldBe Vector("foo", "bar")
    }
    "be able to verify several arguments at the same call" in {
      val probe = mock[Probe]
      probe.multi(17, "seventeen")
      probe.multi(23, List(23))

      var verifiedInts = 0
      var verifiedAnyRefs = 0

      capturing {
        there were two(probe).multi(
          verified[Int] by { case i if i == 17 || i == 23 => verifiedInts += 1 },
          verified[AnyRef] by {
            case s: String =>
              verifiedAnyRefs += 1
              s shouldNot be (empty)
            case l: List[_] =>
              verifiedAnyRefs += 1
              l shouldNot be (empty)
          }
        )
      }

      verifiedInts shouldBe 2
      verifiedAnyRefs shouldBe 2
    }
  }

  trait Probe {
    def single(arg: String)
    def multi(arg1: Int, arg2: AnyRef)
  }
}
