package com.github.jostly.scalatest.mock.mockito

import com.github.jostly.scalatest.mock.mockito.matchers.{OperMatcher, TypeMatcher}
import org.hamcrest.{BaseMatcher, Description, Matcher => HamcrestMatcher}
import org.mockito.{Matchers => M}

import scala.reflect._

trait MockitoMatchers {

  def is[@specialized(Byte, Short, Int, Long, Char, Float, Double, Boolean) T](v: T) = M.eq(v)
  def any[T : ClassTag]: T = M.argThat[T](new TypeMatcher[T])

  def anything[T] = M.any[T]
  def *[T] = anything[T]

  def anyVararg[T] = M.anyVararg[T]()
  def **[T] = anyVararg[T]

  def lessThan[T : ClassTag](v: T)(implicit ordering: Ordering[T]) = oper[T](ordering.lt)("less than", v)
  def lessThanOrEqual[T : ClassTag](v: T)(implicit ordering: Ordering[T]) = oper[T](ordering.lteq)("less than or equal", v)
  def greaterThan[T : ClassTag](v: T)(implicit ordering: Ordering[T]) = oper[T](ordering.gt)("greater than", v)
  def greaterThanOrEqual[T : ClassTag](v: T)(implicit ordering: Ordering[T]) = oper[T](ordering.gteq)("greater than or equal", v)

  private def oper[T : ClassTag](f: (T, T) => Boolean)(operator: String, value: T) = M.argThat[T](new OperMatcher(f, operator, value))

  def argThat[T : ClassTag](f: T => Boolean)(description: String = f.toString()) = M.argThat[T](funToMatcher(f)(description))
  def argThat[T : ClassTag](m: HamcrestMatcher[T]) = M.argThat[T](m)

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





