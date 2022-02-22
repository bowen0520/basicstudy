package com.briup.bigdata.day01
/*
分别为:定义一个类User,要求提供id name age salary四个属性
  i,其中年龄和工资salary是私有的，
  ii,提供伴生对象，并且可以使用User(id)就创建出来一个User对象
  iii,重写toString方法，打印属性值
 */
object Homework1 {
  def adder(m:Int,n:Int)=m&n+3

  def min(m:Int,n:Int):Int={
    var a=m+1
    if(m>n) a=n+1
    return a
  }

  def main(args: Array[String]): Unit = {
    val one=adder(5,6)//7
    val two=adder(5,one)//7
    val three=min(one,two)//
    println(three)
    println(getCode("hello"))
  }

  def getCode(str:String):IndexedSeq[Int]={
    for(v <- str)yield {v-0}
  }
}


