package com.github.jostly.scalatest.mock.mockito

import com.github.jostly.scalatest.mock.mockito.matchers.{SpreadMatcher, WrappedScalaTestMatcher}
import org.mockito.hamcrest.{MockitoHamcrest => H}
import org.scalactic.TripleEqualsSupport.Spread
import org.scalatest.matchers.{Matcher, MatcherFactory1, MatcherFactory2}

import scala.language.{higherKinds, implicitConversions}
import scala.reflect._

trait MockitoScalaTestMatcherAdapters {

  implicit def should[T : ClassTag](matcher: Matcher[T]): T = H.argThat[T](new WrappedScalaTestMatcher(matcher))

  // Method signatures gently lifted from ScalaTest Matchers

  implicit def should[T : ClassTag, TC1[_]](factory: MatcherFactory1[T, TC1])(implicit tc1: TC1[T]): T = {
    H.argThat[T](new WrappedScalaTestMatcher[T](factory.matcher))
  }

  implicit def should[T : ClassTag, TC1[_], TC2[_]](factory: MatcherFactory2[T, TC1, TC2])(implicit tc1: TC1[T], tc2: TC2[T]): T = {
    H.argThat[T](new WrappedScalaTestMatcher[T](factory.matcher))
  }

  def within[T : ClassTag](spread: Spread[T]): T = H.argThat[T](new SpreadMatcher(spread))
}

object MockitoScalaTestMatcherAdapters extends MockitoScalaTestMatcherAdapters

