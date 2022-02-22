package com.briup.bigdata.day05

object Test1 {
  def main(args: Array[String]): Unit = {
    var str = "adasdadaAJHSDsadADfad"
    var map = str.groupBy(c => {
      c.isUpper
    })
    map.foreach(println)
  }
}
