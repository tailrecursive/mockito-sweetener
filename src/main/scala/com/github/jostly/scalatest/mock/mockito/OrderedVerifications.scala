package com.github.jostly.scalatest.mock.mockito

import org.mockito.{Mockito => M}

import scala.language.implicitConversions

trait OrderedVerifications {

  trait Order
  case object NoOrder extends Order
  case class InOrder(io: org.mockito.InOrder, mocks: Seq[AnyRef]) extends Order

  def ordered(mocks: AnyRef*): InOrder = InOrder(M.inOrder(mocks: _*), mocks)

  class Ordered(mocks: AnyRef*) {
    implicit val order = InOrder(M.inOrder(mocks: _*), mocks)
  }

}
