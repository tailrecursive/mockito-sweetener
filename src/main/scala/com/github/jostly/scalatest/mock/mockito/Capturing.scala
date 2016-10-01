package com.github.jostly.scalatest.mock.mockito

import org.mockito.ArgumentCaptor
import org.scalatest.Assertions

import scala.reflect._
import scala.collection.JavaConverters._

trait Capturing {

  val captured = new Captured

  class Captured {
    def get[A](label: String): A =
      TLS.get(label).asInstanceOf[ArgumentCaptor[A]].getValue

    def getAll[A](label: String): Seq[A] =
      TLS.get(label).asInstanceOf[ArgumentCaptor[A]].getAllValues.asScala

    def as[A: ClassTag](label: String): A = {
      val captures = TLS.get
      val captor = ArgumentCaptor.forClass(classTag[A].runtimeClass.asInstanceOf[Class[A]])
      captures.add(label, captor)
      captor.capture()
    }
  }

  def verified[A: ClassTag] = new Verified[A]

  class Verified[A: ClassTag] {
    def by(pf: PartialFunction[A, Any]): A = {
      val captures = TLS.get
      val captor = ArgumentCaptor.forClass(classTag[A].runtimeClass.asInstanceOf[Class[A]])
      captures.add { () =>
        captor.getAllValues.asScala.foreach { v =>
          if (pf.isDefinedAt(v)) pf(v)
          else Assertions.fail(s"Expected a value defined at supplied verification, got $v")
        }
      }
      captor.capture()
    }
  }

  def capturing[A](block: => A): A = {
    try {
      TLS.set()
      val t = block
      TLS.get.verify()
      t
    } finally {
      TLS.clear()
    }
  }
}

object Capturing extends Capturing

class CapturingException(message: String) extends RuntimeException(message)

object TLS {

  private[this] val tls: ThreadLocal[Option[Captures]] = new ThreadLocal[Option[Captures]] {
    override def initialValue(): Option[Captures] = None
  }

  def get: Captures = tls.get() match {
    case Some(v) => v
    case None =>
      throw new CapturingException(
        """Argument capturing must be executed in a capturing block.
          |Try surrounding your verifications with
          |capturing {
          |  ...
          |}""".stripMargin)
  }
  def set() = tls.set(Some(new Captures))
  def clear() = tls.remove()

  class Captures {
    private[this] var captors = Map.empty[String, ArgumentCaptor[_]]
    private[this] var verifications = Vector.empty[() => Any]
    def add[A](label: String, captor: ArgumentCaptor[A]): Unit = captors += (label -> captor)
    def get(label: String): Option[ArgumentCaptor[_]] = captors.get(label)
    def apply(label: String): ArgumentCaptor[_] = captors(label)
    def add(verification: () => Any): Unit = verifications :+= verification
    def verify(): Unit = verifications.foreach(v => v())
  }
}