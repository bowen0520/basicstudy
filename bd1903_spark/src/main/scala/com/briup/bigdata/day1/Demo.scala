package com.briup.bigdata.day1

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Demo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("firstSpark")
    val sc = new SparkContext(conf)
    //val sc = SparkContext.getOrCreate(conf)
    sc.setLogLevel("WARN")
    val rdd = sc.textFile("E:\\aa.txt").flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
    val pr = rdd.foreach(println)
    val of = rdd.saveAsTextFile("E:\\bb.txt")

    sc.stop()
  }
}