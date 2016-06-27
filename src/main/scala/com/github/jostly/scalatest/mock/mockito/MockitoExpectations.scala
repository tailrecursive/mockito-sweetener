package com.github.jostly.scalatest.mock.mockito

import org.mockito.{Mockito => M}
import org.mockito.stubbing.OngoingStubbing

import scala.language.implicitConversions

trait MockitoExpectations {

  implicit class Returns[@specialized(Specializable.Primitives) T](m: => T) {
    def returns(v: T) = M.when(m).thenReturn(v)
  }

  implicit class RichStubbing[@specialized(Specializable.Primitives) T](os: OngoingStubbing[T]) {
    def thenReturns(v: T) = os.thenReturn(v)
    def thenThrows(t: Throwable) = os.thenThrow(t)
  }

  implicit class Throws[@specialized(Specializable.Primitives) T](m: => T) {
    def throws(t: Throwable) = M.when(m).thenThrow(t)
  }

}

object MockitoExpectations extends MockitoExpectations