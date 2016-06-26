package com.github.jostly.scalatest.mock.mockito.matchers

import org.hamcrest.{BaseMatcher, Description}
import org.scalactic.TripleEqualsSupport.Spread

import scala.reflect.ClassTag

class SpreadMatcher[T : ClassTag](spread: Spread[T]) extends BaseMatcher[T] {
  override def matches(item: scala.Any): Boolean = item match {
    case t: T if spread.isWithin(t) => true
    case _ => false
  }
  override def describeTo(description: Description): Unit = {
    description.appendText(spread.toString())
  }
}
