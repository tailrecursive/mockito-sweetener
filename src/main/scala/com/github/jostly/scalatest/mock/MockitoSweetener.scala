package com.github.jostly.scalatest.mock

import com.github.jostly.scalatest.mock.mockito.{MockitoExpectations, MockitoMatchers, MockitoScalaTestMatcherAdapters, MockitoVerifications}
import org.scalatest.mock.MockitoSugar

trait MockitoSweetener extends MockitoSugar with MockitoExpectations with MockitoVerifications with MockitoMatchers with MockitoScalaTestMatcherAdapters

object MockitoSweetener extends MockitoSweetener

