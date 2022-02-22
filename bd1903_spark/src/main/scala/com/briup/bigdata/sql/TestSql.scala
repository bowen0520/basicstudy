package com.briup.bigdata.sql

import org.apache.spark.sql.{SaveMode, SparkSession}

object TestSql {
  def main(args: Array[String]): Unit = {

    val str = "adsad"*3

    val spark = SparkSession.builder()
      .appName("sparksql")
      .master("local")
      .getOrCreate()
    import spark.implicits._

    println(spark)
    //读取文件，获取DataFrame
    val df = spark.read.option("sep"," ").
      csv("D:\\ideal-workplace\\bd1903_spark\\src\\data\\namedata").
      toDF("sid","name","age","gender")  //,"discriber"

    //将DataFrame注册为视图，通过Sql来操作视图
    df.createTempView("students")
    df.show()
    //使用Sql语句操作视图
    val result = spark.sql("select * from students where age=18")
    result.show()

    result.write.format("json").option("path","src/data/json1").mode(SaveMode.Append).save()


    val seq = 1 to 10
    seq.toDS()

    spark.close()
  }
}
