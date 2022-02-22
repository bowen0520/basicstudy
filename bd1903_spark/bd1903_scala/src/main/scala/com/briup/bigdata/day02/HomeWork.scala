package com.briup.bigdata.day02

object HomeWork {
  def main(args: Array[String]): Unit = {
    getNums.foreach(println)
  }
  def getNums :List[Int]={
    var list:List[Int] = List.apply();
    for(num:Int <- 100 to 999){
      var g = num%10
      var s = (num/10)%10
      var b = (num/100)%10
      if(((g*g*g)+(s*s*s)+(b*b*b))==num){
        list = num::list
      }
    }
    list
  }
}
