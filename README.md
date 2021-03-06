# MockitoSweetener

[![](https://travis-ci.org/jostly/mockito-sweetener.svg?branch=master)](https://travis-ci.org/jostly/mockito-sweetener/builds)


Like MockitoSugar for ScalaTest, but sweeter and with fewer calories.

Inspired by the Mockito DSL in Specs2, adapted for use with ScalaTest

## Install

Available to download from [jCenter](https://bintray.com/bintray/jcenter?filterByPkgName=mockito-sweetener)

SBT:

```
libraryDependencies ++= Seq (
  "com.github.jostly" %% "mockito-sweetener" % "0.3.0"
)
```

Maven:

```
<dependency>
  <groupId>com.github.jostly</groupId>
  <artifactId>mockito-sweetener_2.11</artifactId>
  <version>0.3.0</version>
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

## Capture arguments

Mix in the `Capturing` trait to enable argument capturing in verifications. All use of argument captures
must be wrapped in a `capturing { ... }` block:

```scala
capturing {
  // do your captures and verifications here
}
```

### Capture and retrieve

You can capture arguments to a named argument captor using the `captured as "<label>"` argument matcher,
which will store captured arguments under the supplied label. Retrieve the captured arguments
using `captured.get[<your type>](<the label>)`:

```scala
val probe = mock[Probe]
probe.single("foo")

capturing {
  there was one(probe).single(captured as "a")
  captured.get[String]("a") shouldBe "foo"
}
```

If there are multiple calls to your method, you can get all the captured values with
the `captured.getAll` method.

### Verify

If all you want to do is run verfications on the captured arguments, use the
`verified.by(partial function)` construct:

```scala
val probe = mock[Probe]
probe.single("foo")

capturing {
  there was one(probe).single(verified[String] by {
    case s =>
     s shouldBe "foo"
  })
}
```

At the end of the `capturing { ... }` block, the supplied partial function will be evaulated for
all captured values.

## Troubleshooting

Mockito 2 requires at least Java 8 u45. If you see the following error when mocking generic types, 
you most likely need to update your JDK:
```
[info]   org.mockito.exceptions.base.MockitoException: Mockito cannot mock this class: ...
[info]
[info] Mockito can only non-private & non-final classes.
[info] If you're not sure why you're getting this error, please report to the mailing list.
[info]
[info] Java 8 early builds have bugs that were addressed in Java 1.8.0_45, please update your JDK!
```

## Release notes

### x.y.z 

* Upgrade to Mockito 2.1.0
* Mockito is no longer an optional dependency
* Updated dependencies:
    * Mockito `1.10.19` => `2.1.0`
    * Scala XML `1.0.5` => `1.0.6`

### 0.3.0

* Added argument capturing with the `Capturing` trait

### 0.2.0

* Upgraded dependencies:
    * ScalaTest `2.2.6` => `3.0.0` (motivating a minor version bump)
    * Mockito `1.9.0` => `1.10.19`
    * Scala XML `1.0.2` => `1.0.5`
    * Scala `2.11.7` => `2.11.8`
* Removed debug printouts
* Slightly narrowed the specialization of `MockitoMatchers.is`

### 0.1.2

* Added `zero` as an alias for `no` in call number verifications
  to fix a conflict with ScalaTest Matchers

### 0.1.1

* Added specialization where necessary to properly handle cases of matching / stubbing primitives

### 0.1.0

* Initial release