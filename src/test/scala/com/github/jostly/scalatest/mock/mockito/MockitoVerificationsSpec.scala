package com.github.jostly.scalatest.mock.mockito

import com.github.jostly.scalatest.mock.MockitoSweetener
import org.mockito.exceptions.base.MockitoAssertionError
import org.mockito.exceptions.verification._
import org.scalatest.{Matchers, WordSpec}

class MockitoVerificationsSpec extends WordSpec with Matchers with MockitoSweetener {

  trait Probe {
    def parameterless: String
    def first(x: Long): Unit
    def second(i: Int, s: String): Unit
  }

  "verifying calls to a method" when {
    "a single call was made" should {
      val probe = mock[Probe]

      probe.parameterless

      "verify the call with exact number of invocations" in {
        there was one(probe).parameterless
      }
      "verify the call with at least" in {
        there was atLeastOne(probe).parameterless
      }
      "verify the call with at most" in {
        there was atMostOne(probe).parameterless
        there were atMost(2).of (probe).parameterless
      }
      "verify no more calls" in {
        there was one(probe).parameterless
        there were noMoreCallsTo (probe)
      }
      "fail when verifying for two calls" in {
        a[TooLittleActualInvocations] should be thrownBy {
          there were two(probe).parameterless
        }
      }
      "fail when verifying for at least two calls" in {
        a[TooLittleActualInvocations] should be thrownBy {
          there were atLeast (2) (probe).parameterless
        }
      }
      "fail when verifying for no calls" in {
        a[NeverWantedButInvoked] should be thrownBy {
          there were no(probe).parameterless
        }
      }
    }
    "two calls were made" should {
      val probe = mock[Probe]

      probe.parameterless
      probe.parameterless

      "verify all the calls" in {
        there were two(probe).parameterless
      }
      "verify the call with at least" in {
        there was atLeastOne(probe).parameterless
        there were atLeast(2).of (probe).parameterless
      }
      "verify the call with at most" in {
        there were atMost(2).of (probe).parameterless
        there were atMost(3).of (probe).parameterless
      }
      "fail when verifying one call" in {
        a[TooManyActualInvocations] should be thrownBy {
          there was one(probe).parameterless
        }
      }
      "fail when verifying three calls" in {
        a[TooLittleActualInvocations] should be thrownBy {
          there were three(probe).parameterless
        }
      }
      "fail when verifying for at least three calls" in {
        a[TooLittleActualInvocations] should be thrownBy {
          there were atLeast (3).of (probe).parameterless
        }
      }
      "fail when verifying for at most one call" in {
        a[MockitoAssertionError] should be thrownBy {
          there was atMostOne (probe).parameterless
        }
      }
    }
    "three calls were made" should {
      val probe = mock[Probe]

      probe.parameterless
      probe.parameterless
      probe.parameterless

      "verify all the calls" in {
        there were three(probe).parameterless
      }
      "verify the call with at least" in {
        there were atLeast(3).of (probe).parameterless
      }
      "verify the call with at most" in {
        there were atMost(3).of (probe).parameterless
      }
      "fail when verifying one call" in {
        a[TooManyActualInvocations] should be thrownBy {
          there was two(probe).parameterless
        }
      }
      "fail when verifying three calls" in {
        a[TooLittleActualInvocations] should be thrownBy {
          there were 4.of (probe).parameterless
        }
      }
      "fail when verifying for at least three calls" in {
        a[TooLittleActualInvocations] should be thrownBy {
          there were atLeast (4).of (probe).parameterless
        }
      }
      "fail when verifying for at most one call" in {
        a[MockitoAssertionError] should be thrownBy {
          there was atMost(2).of (probe).parameterless
        }
      }
    }
    for (n <- 4 to 10) {
      s"$n calls were made" should {
        val probe = mock[Probe]

        for (i <- 1 to n) {
          probe.parameterless
        }
        "verify all the calls" in {

          there were n.of(probe).parameterless
        }
        s"fail when verifying ${n - 1} calls" in {
          a[TooManyActualInvocations] should be thrownBy {
            there were (n - 1).of(probe).parameterless
          }
        }
        s"fail when verifying ${n + 1} calls" in {
          a[TooLittleActualInvocations] should be thrownBy {
            there were (n + 1).of(probe).parameterless
          }
        }
      }
    }
    "no calls were made" should {
      val probe = mock[Probe]

      "verify no specific calls" in {
        there were no (probe).parameterless
      }
      "verify no calls whatsoever" in {
        there were noCallsTo (probe)
      }
    }
  }

  "verifying multiple calls in no particular order" when {
    "calling several methods on the same mock" should {
      val probe = mock[Probe]

      probe.first(7L)
      probe.second(1, "One")
      probe.second(2, "Two")

      "verify invocations in any order" in {
        there was one(probe).first(7L) and one(probe).second(1, "One") and one(probe).second(2, "Two")
        there was one(probe).second(2, "Two") and one(probe).first(7L) and one(probe).second(1, "One")
      }
      "fail if either of the verifications fail" in {
        a[WantedButNotInvoked] should be thrownBy {
          there was one(probe).first(8L) and one(probe).second(1, "One") and one(probe).second(2, "Two")
        }
        a[WantedButNotInvoked] should be thrownBy {
          there was one(probe).first(7L) and one(probe).second(2, "One") and one(probe).second(2, "Two")
        }
        a[WantedButNotInvoked] should be thrownBy {
          there was one(probe).first(7L) and one(probe).second(1, "One") and one(probe).second(2, "Three")
        }
      }
    }
  }

  "verifying in order" when {
    "calling methods on two mocks" should {
      val probe1 = mock[Probe]("probe1")
      val probe2 = mock[Probe]("probe2")

      probe1.parameterless
      probe2.parameterless

      "verify invocations in correct order" in {
        implicit val order = ordered(probe1, probe2)

        there was one(probe1).parameterless
        there was one(probe2).parameterless
      }
      "fail if invocations are verified out of order" in {
        implicit val order = ordered(probe1, probe2)

        there was one(probe2).parameterless

        a[VerificationInOrderFailure] should be thrownBy {
          there was one(probe1).parameterless
        }
      }
      "specify ordering as fixture" in new Ordered(probe1, probe2) {
        there was one(probe2).parameterless

        a[VerificationInOrderFailure] should be thrownBy {
          there was one(probe1).parameterless
        }
      }
    }
    "verifying no interactions" should {
      "verify all mocks in the order specification" in {
        val probe = mock[Probe]

        new Ordered(probe) {
          there were noCalls
        }

      }
      "fail if a method was called on the mock" in {
        val probe = mock[Probe]

        probe.parameterless

        new Ordered(probe) {
          a[NoInteractionsWanted] should be thrownBy {
            there were noCalls
          }
        }
      }
    }
  }

  "verifying calls using matchers" when {
    "verifying calls to a method with a single parameter" should {
      val probe = mock[Probe]

      probe.first(17L)
      probe.first(23L)

      "succeed if matcher matches actual argument" in {
        there was one(probe).first(is(17L))
        there was one(probe).first(is(23L))
      }
      "not succeed if matcher does not match argument" in {
        a[WantedButNotInvoked] should be thrownBy {
          there was one(probe).first(is(18L))
        }
      }
      "match multiple invocations with wildcard matchers" in {
        there were two(probe).first(*)
      }
    }
  }

  "verifying calls to function mocks" when {
    "a Function0" should {
      val fn = mock[() => Int]

      fn()
      "verify calls" in {
        there was one(fn)()
      }
    }
  }
}
