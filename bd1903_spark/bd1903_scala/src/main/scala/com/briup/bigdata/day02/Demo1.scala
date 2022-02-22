package com.briup.bigdata.day02

import scala.io.StdIn

object Demo1 {
  var l = StdIn.readLine()

  def main(args: Array[String]): Unit = {


    for(i <- 1 until 5 by 2 if(i>1)){
      println(i)
    }

    for(s <- "hello"){
      println(s)
    }

    var rs = for(s <- 1 to 20 if(s%2==0))yield{
      if(s>10)
        s
    }

    println(rs)
  }
}


