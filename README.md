# MockitoSweetener

[![](https://travis-ci.org/jostly/mockito-sweetener.svg?branch=master)](https://travis-ci.org/jostly/mockito-sweetener/builds)


Like MockitoSugar for ScalaTest, but sweeter and with fewer calories.

Inspired by the Mockito DSL in Specs2, adapted for use with ScalaTest

## Install

Available to download from [jCenter](https://bintray.com/bintray/jcenter?filterByPkgName=mockito-sweetener)

SBT:

```
libraryDependencies ++= Seq (
  "com.github.jostly" %% "mockito-sweetener" % "0.2.0"
)
```

Maven:

```
<dependency>
  <groupId>com.github.jostly</groupId>
  <artifactId>mockito-sweetener_2.11</artifactId>
  <version>0.2.0</version>
</dependency>
```

## Set up expectations

```
val m = mock[List[Int]]
m.length returns 17

assert(m.length == 17)

m.contains(any[Int]) returns true

assert(m.contains(5) == true)
```

## Verify calls

```
val m = mock[List[Int]]
m.length

there was one(m).length
there was zero(m).head
```

## Match arguments

```
val m = mock[List[Int]]
m.contains(3) returns true

assert(m.contains(3) == true)
assert(m.contains(5) == false)

m.contains(any[Int]) returns true

assert(m.contains(5) == true)

there was one(m).contains(lessThan(5))
there were two(m).contains(greaterThanOrEqual(5))
there were 3.of(m).contains(*)
```

## Use ScalaTest matchers

You can use any ScalaTest matcher as a Mockito matcher.

```
import org.scalatest.Matchers._

val m = mock[List[Int]]
m(be < 3) returns 17

assert(m(1) == 17)
assert(m(2) == 17)
assert(m(3) == 0)
```

If the method you are mocking is generic, or takes AnyRef,
the implicit conversion might not kick in. Use the `should`
method in that case:

```
val m = mock[List[Int]]
m.contains(be < 3) returns true // will compile, but not work:
assert(m.contains(1) == false)

m.contains(should(be < 3)) returns true
assert(m.contains(1) == true)
```

Since ScalaTest matchers can be combined to form compound
matches, they can be used to set up more complex expectations easily:

```
val m = mock[List[String]]
m(be > 0 and be < 10) returns "Single digit"
```

## Release notes

### 0.2.0

* Upgraded dependencies:
    * ScalaTest `2.2.6` => `3.0.0` (motivating a minor version bump)
    * Mockito `1.9.0` => `1.10.19`
    * Scala XML `1.0.2` => `1.0.5`
    * Scala `2.11.7` => `2.11.8`
* Removed debug printouts
* Slightly narrowed the specialization of `MockitoMatchers.is`

## 0.1.2

* Added `zero` as an alias for `no` in call number verifications
  to fix a conflict with ScalaTest Matchers

## 0.1.1

* Added specialization where necessary to properly handle cases of matching / stubbing primitives

## 0.1.0

* Initial release