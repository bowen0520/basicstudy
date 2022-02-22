package com.briup.bigdata.core


import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

case class People(id:String,name:String,age:Int,gender:String){}

object RDDMain {
  def main(args: Array[String]): Unit = {

    //获取spark context对象
    val conf = new SparkConf()
      .set("spark.app.name","RDDtest ")
      .set("spark.master","local[*]")
    val sc = SparkContext.getOrCreate(conf)

    //获取RDD
    val rdd:RDD[String] = sc.textFile("D:\\ideal-workplace\\bd1903_spark\\src\\data\\namedata")
    val peopleRdd = rdd.map(line => {
      val Array(id,name,age,gender) = line.split(" ")
      People(id,name,age.toInt,gender)
    })
    val peoidRdd = peopleRdd.map(people => (people.id,people))
    println(peoidRdd.partitioner)
    //分区
    val partition = new HashPartitioner(3)
    val rdd2 = peoidRdd.partitionBy(partition)

    println(rdd2.partitioner)
    sc.stop()
  }


}
