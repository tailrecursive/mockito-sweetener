package com.github.jostly.scalatest.mock.mockito

import com.github.jostly.scalatest.mock.MockitoSweetener
import org.scalatest.{Matchers, WordSpec}

class MockitoExpectationsSpec extends WordSpec with Matchers with MockitoSweetener {

  trait Probe {
    def parameterless: String
    def unit(): Unit
    def parameter(a: AnyRef): String
  }

  class ProbeException extends RuntimeException

  "a method with non-Unit return" when {
    "expecting behaviour" should {
      "work for a single return value" in {
        val probe = mock[Probe]

        probe.parameterless returns "Expected response"

        probe.parameterless should be("Expected response")
      }
      "work for chained return values" in {
        val probe = mock[Probe]

        probe.parameterless returns "Response 1" thenReturns "Response 2" thenReturns "Response 3"

        matchAll(probe.parameterless)("Response 1", "Response 2", "Response 3")
      }
      "work for a single throw" in {
        val probe = mock[Probe]

        probe.parameterless throws new ProbeException

        a[ProbeException] should be thrownBy probe.parameterless
      }
      "work for chained throws and return values" in {
        val probe = mock[Probe]
        probe.parameterless throws new ProbeException thenReturns "Response 1" thenReturns "Response 2" thenThrows new ProbeException thenReturns "Response 3"

        a[ProbeException] should be thrownBy probe.parameterless
        matchAll(probe.parameterless)("Response 1", "Response 2")
        a[ProbeException] should be thrownBy probe.parameterless
        probe.parameterless should be("Response 3")
      }
    }
  }

  "a method with Unit return" when {
    "expecting behaviour" should {
      "work for a single throw" in {
        val probe = mock[Probe]

        probe.unit() throws new ProbeException

        a[ProbeException] should be thrownBy probe.unit()
      }
      "work for multiple throws" in {
        val probe = mock[Probe]

        probe.unit() throws new ProbeException thenThrows new IllegalArgumentException

        a[ProbeException] should be thrownBy probe.unit()
        an[IllegalArgumentException] should be thrownBy probe.unit()
      }
    }
  }

  "a method with a parameter" when {
    "expecting behaviour" should {
      "specify concrete expected arguments" in {
        val probe = mock[Probe]

        probe.parameter("0") returns "#0"
        probe.parameter("1") returns "#1"

        probe.parameter("1") should be ("#1")
        probe.parameter("0") should be ("#0")
      }
      "specify expected arguments with matchers" in {
        val probe = mock[Probe]
        val somesuch = List()

        probe.parameter(any[Set[_]]) returns "A Set"
        probe.parameter(any[List[_]]) returns "A List"

        probe.parameter(Set("Whut?")) should be ("A Set")
        probe.parameter(Set(4711)) should be ("A Set")

        probe.parameter(List("Oh")) should be ("A List")
        probe.parameter(List(42)) should be ("A List")
      }
    }
  }

  "a function value" when {
    "it takes no parameters" should {
      "have its return value properly mocked" in {
        val fn = mock[() => Int]

        fn() returns 17

        fn() should be (17)
      }
    }
    "it takes one parameters" should {
      "have its return value properly mocked" in {
        val fn = mock[(Int) => Int]

        fn(0) returns 17

        fn(0) should be (17)
      }
    }
  }

  def matchAll[T](f: => T)(r: T*): Unit = {
    for ((v, i) <- r.zipWithIndex) {
      assert(f === v, s"Mismatch on response #${i + 1}")
    }
  }

}
