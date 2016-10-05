package com.github.jostly.scalatest.mock.mockito

import com.github.jostly.scalatest.mock.mockito.matchers.{OperMatcher, TypeMatcher}
import org.hamcrest.{BaseMatcher, Description}
import org.mockito.hamcrest.{MockitoHamcrest => H}
import org.mockito.{ArgumentMatchers => M}
import org.hamcrest.{Matcher => HamcrestMatcher}

import scala.reflect._

trait MockitoMatchers {

  def is[@specialized(Byte, Short, Int, Long, Char, Float, Double, Boolean) T](v: T) = M.eq(v)
  def any[T : ClassTag]: T = H.argThat[T](new TypeMatcher[T])

  def anything[T] = M.any[T]
  def *[T] = anything[T]

  @deprecated("use anything/* instead", since = "0.4.0")
  def anyVararg[T] = *[T]
  @deprecated("use anything/* instead", since = "0.4.0")
  def **[T] = *[T]

  def lessThan[T : ClassTag](v: T)(implicit ordering: Ordering[T]) = oper[T](ordering.lt)("less than", v)
  def lessThanOrEqual[T : ClassTag](v: T)(implicit ordering: Ordering[T]) = oper[T](ordering.lteq)("less than or equal", v)
  def greaterThan[T : ClassTag](v: T)(implicit ordering: Ordering[T]) = oper[T](ordering.gt)("greater than", v)
  def greaterThanOrEqual[T : ClassTag](v: T)(implicit ordering: Ordering[T]) = oper[T](ordering.gteq)("greater than or equal", v)

  private def oper[T : ClassTag](f: (T, T) => Boolean)(operator: String, value: T) = H.argThat[T](new OperMatcher(f, operator, value))

  def argThat[T : ClassTag](f: T => Boolean)(description: String = f.toString()) = H.argThat[T](funToMatcher(f)(description))
  def argThat[T : ClassTag](m: HamcrestMatcher[T]) = H.argThat[T](m)

  private def funToMatcher[T : ClassTag](f: T => Boolean)(description: String): HamcrestMatcher[T] = new BaseMatcher[T] {
    override def matches(item: Any): Boolean = item match {
      case t: T => f(t)
      case _ => false
    }
    override def describeTo(d: Description): Unit = {
      d.appendText(description)
    }
  }

}

object MockitoMatchers extends MockitoMatchers





