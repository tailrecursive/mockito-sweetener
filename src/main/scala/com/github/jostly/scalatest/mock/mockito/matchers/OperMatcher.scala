package com.github.jostly.scalatest.mock.mockito.matchers

import org.hamcrest.{BaseMatcher, Description}

import scala.reflect.ClassTag

class OperMatcher[T : ClassTag](f: (T, T) => Boolean, operator: String, value: T) extends BaseMatcher[T] {
  override def matches(item: scala.Any): Boolean = item match {
    case t: T => f(t, value)
    case _ => false
  }
  override def describeTo(description: Description): Unit = {
    description.appendText(operator)
    description.appendText(" ")
    description.appendValue(value)
  }
}
