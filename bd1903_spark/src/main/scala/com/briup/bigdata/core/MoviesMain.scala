package com.briup.bigdata.core

import org.apache.spark.{SparkConf, SparkContext}

object MoviesMain {
  def main(args: Array[String]): Unit = {
    //获取spark context对象
    val conf = new SparkConf()
      .set("spark.app.name","movies")
      .set("spark.master","local[*]")
    val sc = SparkContext.getOrCreate(conf)

    //1,男女用户比例
    val users = sc.textFile("E:\\briup\\Spark\\spark\\ml-1m\\ml-1m\\users.dat")
    //数据清洗
    val genderrdd = users.map(line => {
        val msgs = line.split("::")
      (msgs(1),1)
    })
    val genders = genderrdd.reduceByKey(_+_).foreach(println)

    val movierdd = sc.textFile("E:\\briup\\Spark\\spark\\ml-1m\\ml-1m\\ratings.dat")
    val userandrate = movierdd.map(line => {
      val Array(user_id,_,rate,_) = line.split("::")
      (user_id,rate.toDouble)
    })
    val userandArr = userandrate.groupByKey()
    val avgRateRDD =userandArr.mapValues(iter => {
      val list = iter.toList
      val size = list.size
      val sum = list.sum
      sum/size
    })
    //按照评分排序
    avgRateRDD.sortBy(_._2,ascending = false).take(10).foreach(println)

    sc.stop()
  }
}
