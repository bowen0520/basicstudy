package com.briup.bigdata.day03

import scala.collection.mutable.ArrayBuffer

object Test1 {
  def main(args: Array[String]): Unit = {
    val ab1 = new ArrayBuffer
    val ab2 = ArrayBuffer.empty
    val ab3 = ArrayBuffer(1,2)
    println(ab3.apply(1))

    val ArrayBuffer(a,b) = ab3
    println(a)

    ArrayBuffer(5,6) ++=: ab3
    ab3.foreach(print)

    ab3.append(7)
    ab3.foreach(print)
  }
}
