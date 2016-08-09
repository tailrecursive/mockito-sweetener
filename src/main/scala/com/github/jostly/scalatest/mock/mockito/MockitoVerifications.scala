package com.github.jostly.scalatest.mock.mockito

import org.mockito.verification.VerificationMode
import org.mockito.{Mockito => M}

trait MockitoVerifications extends OrderedVerifications {

  def there = new There

  class There {
    def was[T](f: => T) = withAnd(f)
    def were[T](f: => T) = withAnd(f)
  }

  object And {
    def and[T](f: => T) = withAnd(f)
  }

  private def withAnd[T](f: => T): And.type = {
    f
    And
  }

  private def verify[T <: AnyRef](mock: T, mode: VerificationMode, order: Order) = order match {
    case NoOrder => M.verify(mock, mode)
    case InOrder(io, _) => io.verify(mock, mode)
  }

  def verifyNoMoreInteractions(mocks: AnyRef*)(implicit order: Order = NoOrder) = order match {
    case NoOrder => M.verifyNoMoreInteractions(mocks: _*)
    case InOrder(io, _) => io.verifyNoMoreInteractions()
  }

  def verifyZeroInteractions(mocks: AnyRef*) = M.verifyZeroInteractions(mocks: _*)

  def noCalls(implicit inOrder: InOrder) = verifyZeroInteractions(inOrder.mocks: _*)
  def noCallsTo(mock: AnyRef)(implicit order: Order = NoOrder) = verifyZeroInteractions(mock)
  def noMoreCallsTo(mock: AnyRef)(implicit order: Order = NoOrder) = verifyNoMoreInteractions(mock)(order)

  def no[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.never(), order)
  def zero[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.never(), order)
  def one[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.times(1), order)
  def two[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.times(2), order)
  def three[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.times(3), order)

  def atLeastOne[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.atLeast(1), order)
  def atLeast(n: Int) = new AtLeast(n)
  def atMostOne[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.atMost(1), order)
  def atMost(n: Int) = new AtMost(n)

  class AtLeast(n: Int) {
    def of[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.atLeast(n), order)
    def apply[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = of(mock)(order)
  }

  class AtMost(n: Int) {
    def of[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.atMost(n), order)
    def apply[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = of(mock)(order)
  }

  implicit class Of(n: Int) {
    def of = this
    def apply[T <: AnyRef](mock: T)(implicit order: Order = NoOrder) = verify(mock, M.times(n), order)
  }
}

object MockitoVerifications extends MockitoVerifications
