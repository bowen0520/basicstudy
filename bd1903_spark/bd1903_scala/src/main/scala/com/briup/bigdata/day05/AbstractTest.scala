package com.briup.bigdata.day05

class Animal{
  val num:Int = 10
  val list = new Array[String](num)
}

class Pig extends {override val num = 5} with Animal{

}
object Test6 extends App {
  val p = new Pig
  println(p.list.length)

}
