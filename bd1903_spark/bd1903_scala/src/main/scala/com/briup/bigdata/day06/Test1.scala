package com.briup.bigdata.day06

object Test1 {
 def smaller[T <% Comparable[T]](a : T,b : T): T ={
   if(a.compareTo(b) < 0){
     a
   }else{
     b
   }
 }


  def main(args: Array[String]): Unit = {
    val a:String = "hello"
    val b:String = "world"

    val c:Int = 10
    val d:Int = 10
    c.compareTo(d)

    println(smaller(c,d))
  }
}
