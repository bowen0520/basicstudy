package com.briup.bigdata.day05

object Test2 {
  def main(args: Array[String]): Unit = {
    val t = new Teacher
    println(t.age)

   test1(1 )
  }
  def test1(i:Any): Unit ={
    i match{
      case _:Int => println("Int")
      case _:Long => println("Long")
    }

    val a:PartialFunction[Int,String] = {
      case 1 => "1"
      case 2 => "2"
      case _ => "other"
    }

    var list = List(1,2,3,4)
    list.map(a).foreach(println)
  }
}
