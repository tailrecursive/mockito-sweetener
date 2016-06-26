package com.github.jostly.scalatest.mock.mockito.matchers

import org.hamcrest.{BaseMatcher, Description}

import scala.reflect._

class TypeMatcher[T : ClassTag] extends BaseMatcher[T] {
  override def matches(item: scala.Any): Boolean = item match {
    case _: T => true
    case _ => false
  }
  override def describeTo(description: Description): Unit = {
    description.appendText("any[")
    description.appendText(classTag[T].toString)
    description.appendText("]")
  }
}
