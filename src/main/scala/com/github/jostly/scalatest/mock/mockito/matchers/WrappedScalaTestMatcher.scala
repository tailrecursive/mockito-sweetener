package com.github.jostly.scalatest.mock.mockito.matchers

import org.hamcrest.{BaseMatcher, Description}
import org.scalatest.matchers.Matcher

import scala.reflect.ClassTag

class WrappedScalaTestMatcher[T : ClassTag](matcher: Matcher[T]) extends BaseMatcher[T] {
  private val desc = matcher.toString()
  private var lastFailure: String = desc
  override def matches(item: scala.Any): Boolean = {
    item match {
      case t: T =>
        val res = matcher(t)
        if (!res.matches) {
          lastFailure = res.failureMessage
        }
        res.matches
      case _ => false
    }
  }
  override def describeTo(description: Description): Unit = {
    // https://github.com/scalatest/scalatest/issues/920
    // Some descriptions are missing in scalatest generated code, forcing this workaround in order
    // to print _something_ useful to the user
    description.appendText(desc match {
      case "<function1>" => s"Match failed: $lastFailure"
      case _ => s"<$desc>"
    })
  }
}
