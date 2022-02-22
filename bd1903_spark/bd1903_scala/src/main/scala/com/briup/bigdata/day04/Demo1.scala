package com.briup.bigdata.day04
import scala.util.control.Breaks._
object Demo1 {
  def main(args: Array[String]): Unit = {
    var str = "adasdadaAJHSDsadADfad"
    var map = str.groupBy(c => {
      c.isUpper
    })
    map.foreach(println)
    breakable(
      for(i <- 1 to 10){
        if(i == 4){
          break
        }
      }
    )


    //视为不可变得Arraybuffer
    var v = Vector(10,20,30)

    //和懒加载相关
    //在内存中存放数据只保存了，头，尾，步长
    //间隔有规律得连续数据段
    var r = Range(1,3,2)
    var range = 1 to 11 by 2
    var range2 = 1 until 11 by 2

    println(range(5))
    //println(range2(5))
    println("--------------------------------")
    var lt = List(1,3,5,7)

    println(lt.head)
    println(lt.tail)
    println(lt.tail.head)
    println(lt.tail.tail)
    (1 to 10).map(i=>{scala.math.pow(10,i)})

    println("--------------------------------")
    var tl  = new ::(40,Nil)
    30 :: tl
    var ttl = 10 :: 20 :: 30 :: tl

    println(ttl==tl)
    println("--------------------------------")
    //常用得set来自于PreDef 代表一种不可变集
    //想用子类，Scala.coll
    val s = Set(1,2,3,3)
    s.foreach(println)
    println(s.getClass.getName)

    s.map(a=>a+1)



  }

  def test1(x:Int): Unit ={
    val pi = 3.14

    var str = "adasdadaAJHSDsadADfad"
    var map = str.groupBy(c => {
      c.isUpper
    })
    map.foreach(println)
  }
}
