package com.briup.bigdata.day03

import scala.collection.mutable.ArrayBuffer

object SortTest {
  def main(args: Array[String]): Unit = {
    val ss = ArrayBuffer.empty[Student]
    val s1 = new Student("zs",12)
    val s2 = new Student("ls",15)
    val s3 = new Student("ww",13)
    val s4 = new Student("zl",11)
    val s5 = new Student("we",14)
    ss.append(s1)
    ss.append(s2)
    ss.append(s3)
    ss.append(s4)
    ss.append(s5)

    implicit object MyOrdering extends Ordering[Student]{
      override def compare(x: Student, y: Student): Int = {
        -x.name.compareTo(y.name)
      }
    }

    ss.foreach(println)
    println("-------------------------------")
    val ss1 = ss.sortBy(o => o.age)
    ss1.foreach(println)
    println("-------------------------------")
    val ss2 = ss.sortWith((o1,o2) => {
      o1.age>o2.age
    })
    ss2.foreach(println)
    println("-------------------------------")
    val ss3 = ss.sorted
    ss3.foreach(println)
    println("-------------------------------")
    val ss4 = ss.sorted
    ss4.foreach(println)
  }
}
