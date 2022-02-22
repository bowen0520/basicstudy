package com.briup.bigdata.core

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object UnionDemo {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.WARN)
    val conf = new SparkConf().set("spark.master","local[1]").set("spark.app.name","union")
    val sc = SparkContext.getOrCreate(conf)

    val rdd1 = sc.makeRDD(1 to 4)
    val rdd2 = sc.makeRDD(3 to 6)

    println("并集")
    rdd1.union(rdd2).foreach(println)

    println("交集")
    rdd1.intersection(rdd2).foreach(println)

    println("差集")
    rdd1.subtract(rdd2).foreach(println)

    println("笛卡尔集")
    rdd1.cartesian(rdd2).foreach(println)
  }
}
